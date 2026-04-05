import { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import { fetchWithAuth } from "./utils/auth";
import './TaskPage.css';
import './common.css';

const TaskPage = () => {
    const [selectedTask, setSelectedTask] = useState(null);
    const priorities = ["LOWEST", "LOW", "MEDIUM", "HIGH", "HIGHEST"];
    const navigate = useNavigate();
    const token = localStorage.getItem("token");
    const [tasks, setTasks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const storedUserId = localStorage.getItem("userId");
    const userId = storedUserId ? parseInt(storedUserId, 10) : 0;
    const [isEditing, setIsEditing] = useState(false);
    const [formData, setFormData] = useState(selectedTask || {});
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [projects, setProjects] = useState([]);
    const [, setTaskData] = useState(null);

    // AI integration states
    const [aiRecommendation, setAiRecommendation] = useState(null);
    const [isLoadingRecommendation, setIsLoadingRecommendation] = useState(false);
    const [projectMembers, setProjectMembers] = useState([]); // для модального окна

    const [newTask, setNewTask] = useState({
        taskName: '',
        description: '',
        startDate: '',
        endDate: '',
        projectId: '',
        category: '',
        priority: '',
        assignedTo: '',      // AI: добавлено поле исполнителя
        estimatedHours: ''   // AI: добавлено оценочное время
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
            assignedTo: '',
            estimatedHours: ''
        });
        setProjects([]);
        setProjectMembers([]);
        setAiRecommendation(null);
    };

    const navigateToProfilePage = () => navigate("/profile");
    const navigateToProjectPage = () => navigate("/project");
    const navigateToTaskPage = () => navigate("/task");
    const handleLogoClick = () => navigate('/home');

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

    const formatDateTime = (dateString) => {
        if (!dateString) return "";
        const safeDateString = dateString.endsWith("Z") ? dateString : dateString + "Z";
        const date = new Date(safeDateString);
        return date.toLocaleString("ru-RU", {
            year: "numeric",
            month: "short",
            day: "numeric",
            hour: "2-digit",
            minute: "2-digit"
        });
    };

    const handleTaskClick = async (task) => {
        setSelectedTask({ ...task, comments: [] });
        navigate(`/task/${task.taskId}`);
        try {
            await fetchWithAuth(`http://localhost:8081/task/${task.taskId}/view`, { method: "PUT" });
        } catch (error) {
            console.error("Ошибка при обновлении даты просмотра:", error);
        }
    };

    const handleEditToggle = () => {
        if (isEditing) {
            handleSaveTaskChanges();
            setIsEditing(false);
        } else {
            setFormData(selectedTask);
            setIsEditing(true);
        }
    };

    const handleFormChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSaveTaskChanges = async () => {
        if (!token) return;
        try {
            const response = await fetch(`http://localhost:8081/task/${selectedTask.taskId}/update`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(formData),
            });
            if (!response.ok) throw new Error('Ошибка при обновлении задачи');
            const updatedTask = await response.json();
            setTaskData(updatedTask);
            localStorage.setItem('taskData', JSON.stringify(updatedTask));
        } catch (error) {
            console.error('Ошибка:', error);
        }
    };

    // Загрузка участников проекта для модального окна (при выборе проекта)
    const fetchProjectMembersForModal = async (projectId) => {
        if (!projectId || !token) return;
        try {
            const response = await fetch(`http://localhost:8081/project/${projectId}/all-users`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (!response.ok) throw new Error('Failed to load project members');
            const data = await response.json();
            setProjectMembers(data);
        } catch (err) {
            console.error('Ошибка загрузки участников проекта:', err);
            setProjectMembers([]);
        }
    };

    useEffect(() => {
        if (selectedTask?.projectId && isEditing) {
            // для редактирования уже есть отдельная логика, оставим как было
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
            .finally(() => setLoading(false));
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
                if (!projectResponse.ok) throw new Error("Ошибка загрузки проектов");
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
        // Если изменился проект, загружаем его участников
        if (name === 'projectId' && value) {
            fetchProjectMembersForModal(value);
        }
    };

    // AI: запрос рекомендации
    const handleAiRecommendation = async () => {
        if (!newTask.description || !newTask.projectId || !newTask.endDate) {
            alert("Для получения рекомендации заполните описание, проект и дату окончания.");
            return;
        }
        setIsLoadingRecommendation(true);
        setAiRecommendation(null);
        try {
            const response = await fetch('http://localhost:8000/api/v1/recommend', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    text: newTask.description,
                    project_id: parseInt(newTask.projectId, 10),
                    deadline: newTask.endDate + "T23:59:59Z", // добавим время в конец дня
                    strategy: "balanced"
                })
            });
            if (!response.ok) throw new Error('Ошибка при получении рекомендации');
            const data = await response.json();
            setAiRecommendation(data);
        } catch (err) {
            console.error(err);
            alert('Не удалось получить рекомендацию ИИ');
        } finally {
            setIsLoadingRecommendation(false);
        }
    };

    // Применить рекомендацию: заполнить исполнителя и оценочное время
    const applyRecommendation = () => {
        if (!aiRecommendation) return;
        // Найти пользователя в списке участников, чтобы установить его ID
        const recommendedUser = projectMembers.find(member => member.userId === aiRecommendation.recommended_user_id);
        if (recommendedUser) {
            setNewTask(prev => ({
                ...prev,
                assignedTo: recommendedUser.userId,
                estimatedHours: aiRecommendation.estimated_hours
            }));
        } else {
            alert('Рекомендованный пользователь не входит в выбранный проект');
        }
    };

    const handleCreateTask = async () => {
        if (!newTask.taskName.trim() || !newTask.projectId || !newTask.startDate || !newTask.endDate) {
            alert("Все обязательные поля (название, проект, даты) должны быть заполнены.");
            return;
        }
        if (!token) {
            alert("Пользователь не авторизован");
            return;
        }

        // Подготовка данных для отправки (assignedTo и estimatedHours)
        const taskPayload = {
            taskName: newTask.taskName,
            description: newTask.description,
            startDate: newTask.startDate,
            endDate: newTask.endDate,
            projectId: parseInt(newTask.projectId, 10),
            category: newTask.category,
            priority: newTask.priority,
            assignedTo: newTask.assignedTo || userId,   // если не выбрано, ставим текущего
            estimatedHours: newTask.estimatedHours ? parseFloat(newTask.estimatedHours) : null
        };

        try {
            const response = await fetch('http://localhost:8081/task/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(taskPayload),
            });
            if (!response.ok) throw new Error("Ошибка при создании задачи");
            const createdTask = await response.json();
            console.log("Задача создана:", createdTask);
            resetModalState();
            setIsModalOpen(false);
            // Обновить список задач (можно перезагрузить)
            fetchWithAuth("http://localhost:8081/task/recent")
                .then(res => res.json())
                .then(data => setTasks(data));
        } catch (error) {
            console.error("Ошибка:", error);
            alert("Произошла ошибка при создании задачи.");
        }
    };

    const [newCommentText, setNewCommentText] = useState('');
    const handleAddComment = async () => {
        if (!newCommentText.trim()) return;
        try {
            const response = await fetch(`http://localhost:8081/task/${selectedTask.taskId}/add-comment`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify({ content: newCommentText }),
            });
            if (!response.ok) throw new Error('Ошибка при добавлении комментария');
            await fetchComments(selectedTask.taskId);
            setNewCommentText('');
        } catch (err) {
            console.error(err);
            alert('Не удалось добавить комментарий');
        }
    };

    const fetchComments = async (taskId) => {
        try {
            const response = await fetch(`http://localhost:8081/task/${taskId}/comments`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (!response.ok) throw new Error("Ошибка при загрузке комментариев");
            const data = await response.json();
            setSelectedTask(prev => ({ ...prev, comments: data }));
        } catch (err) {
            console.error(err);
        }
    };

    // Для редактирования задачи (оставляем как есть, но можно добавить аналогичную AI-кнопку, пока не требуется)
    const [projectMembersForEdit, setProjectMembersForEdit] = useState([]);
    useEffect(() => {
        if (selectedTask?.projectId && isEditing) {
            const fetchMembers = async () => {
                try {
                    const response = await fetch(`http://localhost:8081/project/${selectedTask.projectId}/all-users`, {
                        headers: { 'Authorization': `Bearer ${token}` }
                    });
                    if (!response.ok) throw new Error('Failed to load project members');
                    const data = await response.json();
                    setProjectMembersForEdit(data);
                } catch (err) {
                    console.error(err);
                }
            };
            fetchMembers();
        }
    }, [isEditing, selectedTask?.projectId, token]);

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
                                        <div key={index} className="item-card" onClick={() => handleTaskClick(task)}>
                                            <div className="item-header">
                                                <div className="item-title">{task.taskName}</div>
                                                <div className={`item-priority ${priorityClass}`}>{icon}</div>
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
                    <button className="create-button" onClick={() => setIsModalOpen(true)}>
                        + Создать новую задачу
                    </button>
                </div>

                {/* Модальное окно создания задачи */}
                {isModalOpen && (
                    <div className="modal-overlay" onClick={() => { resetModalState(); setIsModalOpen(false); }}>
                        <div className="modal" onClick={(e) => e.stopPropagation()}>
                            <h2>Создать новую задачу</h2>

                            <label>Название задачи:</label>
                            <input type="text" name="taskName" value={newTask.taskName} onChange={handleInputChange} />

                            <label>Описание задачи:</label>
                            <textarea name="description" value={newTask.description} onChange={handleInputChange} />

                            <label>Дата начала:</label>
                            <input type="date" name="startDate" value={newTask.startDate} onChange={handleInputChange} />

                            <label>Дата окончания (дедлайн):</label>
                            <input type="date" name="endDate" value={newTask.endDate} onChange={handleInputChange} />

                            <label>Выберите проект:</label>
                            <select name="projectId" value={newTask.projectId} onChange={handleInputChange}>
                                <option value="">Выберите проект</option>
                                {projects.map((project) => (
                                    <option key={project.projectId} value={project.projectId}>
                                        {project.projectName}
                                    </option>
                                ))}
                            </select>

                            <label>Категория:</label>
                            <input type="text" name="category" value={newTask.category} onChange={handleInputChange} />

                            <label>Приоритет:</label>
                            <select name="priority" value={newTask.priority} onChange={handleInputChange}>
                                <option value="">Выберите приоритет</option>
                                <option value="LOWEST">LOWEST</option>
                                <option value="LOW">LOW</option>
                                <option value="MEDIUM">MEDIUM</option>
                                <option value="HIGH">HIGH</option>
                                <option value="HIGHEST">HIGHEST</option>
                            </select>

                            {/* Поле исполнителя */}
                            <label>Исполнитель:</label>
                            <select name="assignedTo" value={newTask.assignedTo} onChange={handleInputChange}>
                                <option value="">-- Выберите исполнителя --</option>
                                {projectMembers.map(member => (
                                    <option key={member.userId} value={member.userId}>
                                        {member.email}
                                    </option>
                                ))}
                            </select>

                            {/* Поле оценочного времени */}
                            <label>Оценочное время (часы):</label>
                            <input
                                type="number"
                                step="0.5"
                                name="estimatedHours"
                                value={newTask.estimatedHours}
                                onChange={handleInputChange}
                                placeholder="Например, 4.5"
                            />

                            {/* Кнопка ИИ-рекомендации */}
                            <button
                                type="button"
                                onClick={handleAiRecommendation}
                                disabled={isLoadingRecommendation}
                                style={{ marginTop: '10px', backgroundColor: '#4a90e2', color: 'white' }}
                            >
                                {isLoadingRecommendation ? 'Загрузка...' : '🤖 Рекомендация ИИ'}
                            </button>

                            {/* Отображение рекомендации */}
                            {aiRecommendation && (
                                <div style={{ marginTop: '10px', padding: '10px', border: '1px solid #ccc', borderRadius: '5px', backgroundColor: '#f9f9f9' }}>
                                    <p><strong>Рекомендация ИИ:</strong></p>
                                    <p>Исполнитель: {projectMembers.find(m => m.userId === aiRecommendation.recommended_user_id)?.email || aiRecommendation.recommended_user_id}</p>
                                    <p>Прогноз времени: {aiRecommendation.estimated_hours} ч</p>
                                    <button onClick={applyRecommendation} style={{ backgroundColor: '#28a745', color: 'white' }}>
                                        Применить рекомендацию
                                    </button>
                                </div>
                            )}

                            <div className="modal-buttons">
                                <button onClick={handleCreateTask}>Создать</button>
                                <button onClick={() => { resetModalState(); setIsModalOpen(false); }}>Отмена</button>
                            </div>
                        </div>
                    </div>
                )}

                {/* Правая панель с деталями задачи и комментариями (без изменений, но для краткости оставлю) */}
                <div className="content-area">
                    {selectedTask ? (
                        <>
                            <div className="task-details">
                                <h2>
                                    {isEditing ? (
                                        <textarea name="taskName" value={formData.taskName || ''} onChange={handleFormChange} />
                                    ) : (
                                        selectedTask.taskName
                                    )}
                                </h2>
                                <p><strong>Описание:</strong>
                                    {isEditing ? (
                                        <textarea name="description" value={formData.description || ''} onChange={handleFormChange} />
                                    ) : (
                                        selectedTask.description
                                    )}
                                </p>
                                <p><strong>Назначено на:</strong>
                                    {isEditing ? (
                                        <div className="form-group">
                                            <select name="assignedTo" value={formData.assignedTo || ''} onChange={handleFormChange}>
                                                <option value="">-- Выберите исполнителя --</option>
                                                {projectMembersForEdit.map(member => (
                                                    <option key={member.userId} value={member.userId}>{member.email}</option>
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
                                        <input name="category" value={formData.category || ''} onChange={handleFormChange} />
                                    ) : selectedTask.category}
                                </p>
                                <p><strong>Приоритет:</strong>
                                    {isEditing ? (
                                        <select name="priority" value={formData.priority} onChange={(e) => setFormData({ ...formData, priority: e.target.value })}>
                                            {priorities.map((p) => (<option key={p} value={p}>{p}</option>))}
                                        </select>
                                    ) : (
                                        selectedTask.priority
                                    )}
                                </p>
                                <p><strong>Проект:</strong> {selectedTask.projectName || 'Не указано'}</p>
                                <button className="edit-button" onClick={handleEditToggle}>
                                    {isEditing ? 'Сохранить изменения' : 'Редактировать'}
                                </button>
                            </div>
                            <div className="comments-section">
                                <h3>Комментарии</h3>
                                <div className="comments-list">
                                    {(selectedTask.comments || []).length > 0 ? (
                                        selectedTask.comments.map(comment => (
                                            <div key={comment.commentId} className="comment-item">
                                                <div className="comment-header">
                                                    <span className="comment-author">{comment.authorName}</span>
                                                    <span className="comment-date">{formatDateTime(comment.date)}</span>
                                                </div>
                                                <div className="comment-body">{comment.content}</div>
                                            </div>
                                        ))
                                    ) : (
                                        <p>Пока нет комментариев</p>
                                    )}
                                </div>
                                <div className="new-comment">
                                    <textarea placeholder="Добавьте комментарий..." value={newCommentText} onChange={(e) => setNewCommentText(e.target.value)} />
                                    <button className="edit-button" onClick={handleAddComment}>Добавить комментарий</button>
                                </div>
                            </div>
                        </>
                    ) : (
                        <div className="default-hint">Для более подробного просмотра задачи нажмите на неё</div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default TaskPage;