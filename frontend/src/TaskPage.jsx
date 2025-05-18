import React, { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import { fetchWithAuth } from "./utils/auth";
import { useParams } from 'react-router-dom';
import './TaskPage.css';
import './common.css';

const TaskPage = () => {
  const [selectedTask, setSelectedTask] = useState(null);

  const priorities = ["LOWEST", "LOW", "MEDIUM", "HIGH", "HIGHEST"];

  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const [currentUserId, setCurrentUserId] = useState('');

  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const storedUserId = localStorage.getItem("userId");
  const userId = storedUserId ? parseInt(storedUserId, 10) : 0;

  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState(selectedTask || {});

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [newTask, setNewTask] = useState({
    taskId: '',
    taskName: '',
    description: '',
    email: '',
    startDate: '',
    endDate: '',
    projectId: '',
    projectName: '',
    category: '',
    priority: '',
    assignedTo: ''
  });

  const resetModalState = () => {
    setNewTask({
      taskName: '',
      description: '',
      email: '',
      startDate: '',
      endDate: '',
      projectId: '',
      projectName: '',
      category: '',
      priority: '',
      assignedTo: ''
    });
    setProjects([]);
  };

  const [projects, setProjects] = useState([]);

  const navigateToProfilePage = () => {
    navigate("/profile");
  };

  const navigateToProjectPage = () => {
    navigate("/project");
  };

  const navigateToTaskPage = () => {
    navigate("/task");
  };

  const handleLogoClick = () => {
    navigate('/home');
  };

  const getPriorityIcon = (priority) => {
    const iconMap = {
        LOWEST: { icon: "⇊", class: "priority-lowest" },
        LOW: { icon: "↓", class: "priority-low" },
        MEDIUM: { icon: "=", class: "priority-medium" },
        HIGH: { icon: "↑", class: "priority-high" },
        HIGHEST: { icon: "⇈", class: "priority-highest" }
    };
    return iconMap[priority] || { icon: "", class: "" };
  };

  const formatDate = (dateString) => {
      const date = new Date(dateString);
      return date.toLocaleDateString('ru-RU', {
          year: 'numeric',
          month: 'short',
          day: 'numeric'
      });
  };

  const handleTaskClick = async (task) => {
    setSelectedTask(task);
    console.log('Выбранная задача:', task);
    navigate(`/task/${task.taskId}`);

    // Обновление даты последнего просмотра на бэке
    try {
      await fetchWithAuth(`http://localhost:8081/task/${task.taskId}/view`, {
        method: "PUT",
      });
    } catch (error) {
      console.error("Ошибка при обновлении даты просмотра:", error);
    }
  };

  const handleEditToggle = () => {
    if (isEditing) {
      handleSaveTaskChanges();
    } else {
      setFormData(selectedTask);
      setIsEditing(true);
    }
    setIsEditing(!isEditing);
  };

  const handleFormChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSaveTaskChanges = async () => {
    if (!token) {
      console.error("Token is not defined");
      return;
    }

    try {
        const response = await fetch(`http://localhost:8081/task/${selectedTask.taskId}/update`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        throw new Error('Ошибка при обновлении профиля');
      }

      const updatedTask = await response.json();
      const updatedTaskData = {
        ...selectedTask,
        taskDTO: updatedTask,
      };
      setTaskData(updatedTaskData);
      localStorage.setItem('taskData', JSON.stringify(updatedTaskData));

    } catch (error) {
      console.error('Ошибка:', error);
    }
  };

  const [projectMembers, setProjectMembers] = useState([]);

  useEffect(() => {
    if (selectedTask) {
      setFormData(selectedTask);
    }
  }, [selectedTask]);

  useEffect(() => {
    const fetchProjectMembers = async () => {
      if (!selectedTask?.projectId || !token) return;
      try {
        const response = await fetch(`http://localhost:8081/project/${selectedTask.projectId}/all-users`, {
          headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) throw new Error('Failed to load project members');
        const data = await response.json();
        setProjectMembers(data);
      } catch (err) {
        console.error('Ошибка загрузки участников проекта:', err);
      }
    };

    if (isEditing) {
      fetchProjectMembers();
    }
  }, [isEditing, selectedTask?.projectId, token]);

  useEffect(() => {
      fetchWithAuth("http://localhost:8081/task/recent")
          .then(response => {
              if (!response.ok) throw new Error("Ошибка при получении задач");
              return response.json();
          })
          .then(data => setTasks(data))
          .catch(error => setError(error.message))
          .finally(() => setLoading(false)); // <-- завершили загрузку
  }, []);

  useEffect(() => {
    const fetchProjectsAndUsers = async () => {
      if (!token || !isModalOpen) return;

      try {
        const projectResponse = await fetch('http://localhost:8081/project/all', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          }
        });

        if (!projectResponse.ok) {
          throw new Error("Ошибка загрузки проектов");
        }

        const projects = await projectResponse.json();
        setProjects(projects);

      } catch (error) {
        console.error('Ошибка при получении данных:', error);
      }
    };

    fetchProjectsAndUsers();
  }, [isModalOpen, token]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewTask(prev => ({ ...prev, [name]: value }));
  };

  const handleCreateTask = async () => {
    if (!newTask.taskName.trim() || !newTask.projectId || !newTask.startDate || !newTask.endDate) {
      alert("Все поля обязательны для заполнения.");
      return;
    }

    if (!token) {
      alert("Пользователь не авторизован");
      return;
    }

  const taskWithUser = {
    ...newTask,
    assignedTo: userId
  };

    try {
      const response = await fetch('http://localhost:8081/task/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(taskWithUser),
      });

      if (!response.ok) {
        throw new Error("Ошибка при создании задачи");
      }

      const createdTask = await response.json();
      console.log("Задача создана:", createdTask);

      resetModalState();
      setIsModalOpen(false);

    } catch (error) {
      console.error("Ошибка:", error);
      alert("Произошла ошибка при создании задачи.");
    }
  };

  return (
    <div className="base-page">
      <div className="navbar">
        <div className="logo" onClick={handleLogoClick}></div>
        <div className="nav-buttons-group">
          <button className="nav-button">Отчеты</button>
          <button className="nav-button" onClick={navigateToTaskPage}>Задачи</button>
          <button className="nav-button" onClick={navigateToProjectPage}>Проекты</button>
          <button className="nav-button" onClick={navigateToProfilePage}>Профиль</button>
        </div>
      </div>

      <div className="main-content">
      <div className="left-panel">
        <div className="sidebar">
          <div className="sidebar-header">Мои недавние задачи:</div>
          <div className="item-card-group">
              {loading ? (
                  <p className="loader">Загрузка задач...</p>
              ) : error ? (
                  <p style={{ color: "red" }}>Ошибка: {error}</p>
              ) : tasks.length > 0 ? (
                  tasks.map((task, index) => {
                      const { icon, class: priorityClass } = getPriorityIcon(task.priority);
                      return (
                          <div
                            key={index}
                            className="item-card"
                            onClick={() => handleTaskClick(task)}>
                              <div className="item-header">
                                  <div className="item-title">{task.taskName}</div>
                                  <div className={`item-priority ${priorityClass}`}>
                                      {icon}
                                  </div>
                                  <div className="item-deadline">{formatDate(task.startDate)}</div>
                              </div>
                              <div className="item-body">{task.description}</div>
                          </div>
                      );
                  })
              ) : (
                  <p>Задач пока нет</p>
              )}
          </div>
        </div>

{/*         <div className="content-area"> */}
          <button className="create-button" onClick={() => setIsModalOpen(true)}>
            + Создать новую задачу
          </button>
{/*         </div> */}
{/*       </div> */}

          {isModalOpen && (
            <div className="modal-overlay" onClick={() => {resetModalState();
                                                           setIsModalOpen(false);}}>
              <div className="modal" onClick={(e) => e.stopPropagation()}>
                <h2>Создать новую задачу</h2>

                <label>Название задачи:</label>
                <input
                  type="text"
                  name="taskName"
                  value={newTask.taskName}
                  onChange={handleInputChange}
                />

                <label>Описание задачи:</label>
                <textarea
                  name="description"
                  value={newTask.description}
                  onChange={handleInputChange}
                />

                <label>Дата начала:</label>
                <input
                  type="date"
                  name="startDate"
                  value={newTask.startDate}
                  onChange={handleInputChange}
                />

                <label>Дата окончания:</label>
                <input
                  type="date"
                  name="endDate"
                  value={newTask.endDate}
                  onChange={handleInputChange}
                />

                <label>Выберите проект:</label>
                <select
                  name="projectId"
                  value={newTask.projectId}
                  onChange={handleInputChange}
                >
                  <option value="">Выберите проект</option>
                  {projects.map((project) => (
                    <option key={project.projectId} value={project.projectId}>
                      {project.projectName}
                    </option>
                  ))}
                </select>

                <label>Категория:</label>
                <input
                  type="text"
                  name="category"
                  value={newTask.category}
                  onChange={handleInputChange}
                />

                <label>Приоритет:</label>
                <select
                  name="priority"
                  value={newTask.priority}
                  onChange={handleInputChange}
                >
                  <option value="">Выберите приоритет</option>
                  <option value="LOWEST">LOWEST</option>
                  <option value="LOW">LOW</option>
                  <option value="MEDIUM">MEDIUM</option>
                  <option value="HIGHEST">HIGHEST</option>
                  <option value="HIGH">HIGH</option>
                </select>


                <div className="modal-buttons">
                  <button onClick={handleCreateTask}>Создать</button>
                  <button onClick={() => {resetModalState();
                                          setIsModalOpen(false);}}>Отмена</button>
                </div>
              </div>
            </div>
          )}
        </div>

        <div className="content-area">
          {selectedTask ? (
            <div className="task-details">
              <h2>
                {isEditing ? (
                  <textarea
                    name="taskName"
                    value={formData.taskName || ''}
                    onChange={handleInputChange}
                  />
                ) : selectedTask.taskName}
              </h2>

              <p><strong>Описание:</strong>
                {isEditing ? (
                  <textarea
                    name="description"
                    value={formData.description || ''}
                    onChange={handleInputChange}
                  />
                ) : selectedTask.description}
              </p>

              <p><strong>Назначено на:</strong>
               {isEditing ? (
                 <div className="form-group">
                   <select
                     name="assignedTo"
                     value={formData.assignedTo || ''}
                     onChange={handleFormChange}
                   >
                     <option value="">-- Выберите исполнителя --</option>
                     {projectMembers.map(member => (
                       <option key={member.userId} value={member.userId}>
                         {member.email}
                       </option>
                     ))}
                   </select>
                 </div>
               ) : (
                 selectedTask.email
               )}
              </p>
              <p><strong>Дата начала:</strong> {formatDate(selectedTask.startDate)}</p>
              <p><strong>Дата окончания:</strong> {formatDate(selectedTask.endDate)}</p>

              <p><strong>Категория:</strong>
                {isEditing ? (
                  <input
                    name="category"
                    value={formData.category || ''}
                    onChange={handleInputChange}
                  />
                ) : selectedTask.category}
              </p>

              <p><strong>Приоритет:</strong>
                {isEditing ? (
                  <select
                    name="priority"
                    value={formData.priority}
                    onChange={(e) => setFormData({ ...formData, priority: e.target.value })}
                  >
                    {priorities.map((p) => (
                      <option key={p} value={p}>
                        {p}
                      </option>
                    ))}
                  </select>
                ) : selectedTask.priority}
{/*              : ( */}
             {/*      <span>{formData.priority}</span> */}
{/*                 )} */}
              </p>

              <p><strong>Проект:</strong>
                {selectedTask.projectName || 'Не указано'}
              </p>

              <button className="edit-button" onClick={handleEditToggle}>
                {isEditing ? 'Сохранить изменения' : 'Редактировать'}
              </button>
            </div>
          ) : (
            <div className="default-hint">
              Для более подробного просмотра задачи нажмите на неё
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default TaskPage;
