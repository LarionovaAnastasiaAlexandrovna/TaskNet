import React, { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import './TaskPage.css';
import './common.css';

const TaskPage = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const [currentUserId, setCurrentUserId] = useState('');

  const storedUserId = localStorage.getItem("userId");
  const userId = storedUserId ? parseInt(storedUserId, 10) : 0;

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [newTask, setNewTask] = useState({
    taskName: '',
    description: '',
    startDate: '',
    endDate: '',
    projectId: '',
    category: '',
    priority: '',
    assignedTo: ''
  });

  const resetModalState = () => {
    setNewTask({
      taskName: '',
      description: '',
      startDate: '',
      endDate: '',
      projectId: '',
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
        <div className="sidebar">
          <div className="item-card-group">
            <div className="item-card">
              <div className="item-header">
                <div className="item-title">Task Title</div>
                <div className="item-deadline">Deadline</div>
                <div className="item-status">Status</div>
              </div>
              <div className="item-body">Task Description</div>
            </div>
            {/* Добавить больше карточек задач */}
          </div>
        </div>

        <div className="content-area">
          <button className="create-task-button" onClick={() => setIsModalOpen(true)}>
            + Создать новую задачу
          </button>

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
                  <option value="LOW">LOWEST</option>
                  <option value="LOW">LOW</option>
                  <option value="MEDIUM">MEDIUM</option>
                  <option value="HIGH">HIGHEST</option>
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
      </div>
    </div>
  );
};

export default TaskPage;
