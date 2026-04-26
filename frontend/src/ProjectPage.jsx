import React, { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import { fetchWithAuth } from "./utils/auth";
import './ProjectPage.css';
import './common.css';

const ProjectPage = () => {

    const [isMembersModalOpen, setIsMembersModalOpen] = useState(false);
    const [isTasksModalOpen, setIsTasksModalOpen] = useState(false);
    const [projectUsers, setProjectUsers] = useState([]);
    const [newUserEmail, setNewUserEmail] = useState('');

    const navigate = useNavigate();
    const token = localStorage.getItem("token");

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [newProject, setNewProject] = useState({
        projectName: '',
        description: '',
        startDate: '',
        endDate: ''
    });

    const [selectedProject, setSelectedProject] = useState(null);
    const [projects, setProjects] = useState([]);

    const [projectTasks, setProjectTasks] = useState([]);
    const [loadingTasks, setLoadingTasks] = useState(false);

    const handleLogoClick = () => navigate('/home');
    const navigateToTaskPage = () => navigate("/task");
    const navigateToAnalytics = () => navigate("/analytics");

    const navigateToProfilePage = () => {
        fetchWithAuth("http://localhost:8081/profile")
            .then(response => {
                if (!response.ok) throw new Error("Ошибка при получении профиля");
                return response.json();
            })
            .then(data => {
                localStorage.setItem("profileData", JSON.stringify(data));
                navigate("/profile");
            })
            .catch(error => {
                console.error("Ошибка при загрузке профиля:", error);
                alert("Не удалось загрузить профиль");
            });
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewProject(prev => ({ ...prev, [name]: value }));
    };

    const handleCreateProject = async () => {
        if (!newProject.projectName.trim()) {
            alert("Название проекта обязательно");
            return;
        }
        if (!token) {
            alert("Пользователь не авторизован");
            return;
        }

        try {
            const response = await fetch('http://localhost:8081/project/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(newProject),
            });

            if (!response.ok) throw new Error("Ошибка при создании проекта");

            const createdProject = await response.json();
            setProjects(prev => [...prev, createdProject]);
            setSelectedProject(createdProject);
            setIsModalOpen(false);
            setNewProject({ projectName: '', description: '', startDate: '', endDate: '' });

        } catch (error) {
            console.error("Ошибка:", error);
            alert("Произошла ошибка при создании проекта.");
        }
    };

    const fetchProjectUsers = async (projectId) => {
        try {
            const response = await fetch(`http://localhost:8081/project/${projectId}/all-users`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (!response.ok) throw new Error('Ошибка при загрузке участников проекта');
            const users = await response.json();
            setProjectUsers(users);
        } catch (error) {
            console.error(error);
            alert('Не удалось загрузить участников');
        }
    };

    const fetchProjectTasks = async (projectId) => {
        setLoadingTasks(true);
        try {
            const response = await fetch(`http://localhost:8081/project/${projectId}/tasks`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (!response.ok) throw new Error('Ошибка загрузки задач проекта');
            const tasks = await response.json();
            setProjectTasks(tasks);
        } catch (error) {
            console.error(error);
            setProjectTasks([]);
        } finally {
            setLoadingTasks(false);
        }
    };

    const handleOpenMembersModal = () => {
        if (!selectedProject) return;
        fetchProjectUsers(selectedProject.projectId);
        setIsMembersModalOpen(true);
    };

    const handleOpenTasksModal = () => {
        if (!selectedProject) return;
        fetchProjectTasks(selectedProject.projectId);
        setIsTasksModalOpen(true);
    };

    const handleAddUserToProject = async () => {
        try {
            const response = await fetch(`http://localhost:8081/project/${selectedProject.projectId}/add-user`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({ email: newUserEmail })
            });

            if (!response.ok) throw new Error('Ошибка при добавлении пользователя');

            setNewUserEmail('');
            fetchProjectUsers(selectedProject.projectId);
        } catch (error) {
            console.error(error);
            alert('Ошибка при добавлении пользователя');
        }
    };

    const handleSelectProject = (project) => {
        setSelectedProject(project);
    };

    useEffect(() => {
        const fetchProjectsAndUsers = async () => {
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
    }, [token]);

    return (
        <div className="base-page">
            <div className="navbar">
                <div className="logo" onClick={handleLogoClick}></div>
                <div className="nav-buttons-group">
                    <button className="nav-button" onClick={navigateToTaskPage}>Задачи</button>
                    <button className="nav-button" onClick={navigateToAnalytics}>Аналитика</button>
                    <button className="nav-button" onClick={navigateToProfilePage}>Профиль</button>
                </div>
            </div>

            <div className="main-content">
                <div className="left-panel">
                    <div className="sidebar">
                        <div className="item-card-group">
                            {projects.map((project, index) => (
                                <div
                                    key={index}
                                    className="item-card"
                                    onClick={() => handleSelectProject(project)}
                                >
                                    <div className="item-header">
                                        <div className="item-title">{project.projectName}</div>
                                        <div className="item-deadline">{project.endDate || "Без срока"}</div>
                                    </div>
                                    <div className="item-body">{project.description}</div>
                                </div>
                            ))}
                        </div>
                    </div>

                    <button className="create-button" onClick={() => setIsModalOpen(true)}>
                        + Создать новый проект
                    </button>
                </div>

                <div className="content-area">
                    {selectedProject ? (
                        <>
                            <div className="sidebar-header">Проект: {selectedProject.projectName}</div>
                            <div className="project-info">
                                <div className="project-description">
                                    Описание проекта: {selectedProject.description}
                                </div>
                                <div className="project-deadline">
                                    Сроки: с {selectedProject.startDate || "не указано"} до {selectedProject.endDate || "не указано"}
                                </div>
                            </div>
                            <div className="project-details">
                                <h2>Детали проекта</h2>
                                <div className="project-status">
                                    Статус: {selectedProject.status || "Не указан"}
                                </div>
                                <div className="project-buttons">
                                    <button className="add-members-button" onClick={handleOpenMembersModal}>
                                        Участники
                                    </button>
                                    <button className="add-members-button" onClick={handleOpenTasksModal}>
                                        Задачи проекта
                                    </button>
                                </div>
                            </div>
                        </>
                    ) : (
                        <div className="sidebar-header">Выберите проект слева или создайте новый</div>
                    )}
                </div>
            </div>

            {/* Модальное окно создания проекта */}
            {isModalOpen && (
                <div className="modal-overlay" onClick={() => setIsModalOpen(false)}>
                    <div className="modal" onClick={(e) => e.stopPropagation()}>
                        <h2>Создать новый проект</h2>
                        <label>Название проекта:</label>
                        <input
                            type="text"
                            name="projectName"
                            value={newProject.projectName}
                            onChange={handleInputChange}
                        />
                        <label>Описание проекта:</label>
                        <textarea
                            name="description"
                            value={newProject.description}
                            onChange={handleInputChange}
                        />
                        <label>Дата начала:</label>
                        <input
                            type="date"
                            name="startDate"
                            value={newProject.startDate}
                            onChange={handleInputChange}
                        />
                        <label>Дата окончания (опционально):</label>
                        <input
                            type="date"
                            name="endDate"
                            value={newProject.endDate}
                            onChange={handleInputChange}
                        />
                        <div className="modal-buttons">
                            <button onClick={handleCreateProject}>Создать</button>
                            <button onClick={() => setIsModalOpen(false)}>Отмена</button>
                        </div>
                    </div>
                </div>
            )}

            {/* Модальное окно участников */}
            {isMembersModalOpen && (
                <div className="modal-overlay" onClick={() => setIsMembersModalOpen(false)}>
                    <div className="modal" onClick={(e) => e.stopPropagation()}>
                        <h2>Участники проекта</h2>
                        <input
                            type="email"
                            placeholder="Введите email"
                            value={newUserEmail}
                            onChange={(e) => setNewUserEmail(e.target.value)}
                        />
                        <button onClick={handleAddUserToProject}>Добавить</button>
                        <ul>
                            {projectUsers.map(user => (
                                <li key={user.userId}>
                                    {user.userName} ({user.email})
                                </li>
                            ))}
                        </ul>
                        <button onClick={() => setIsMembersModalOpen(false)}>Закрыть</button>
                    </div>
                </div>
            )}

            {/* Модальное окно задач проекта */}
            {isTasksModalOpen && (
                <div className="modal-overlay" onClick={() => setIsTasksModalOpen(false)}>
                    <div className="modal tasks-modal" onClick={(e) => e.stopPropagation()}>
                        <h2>Задачи проекта: {selectedProject?.projectName}</h2>
                        {loadingTasks ? (
                            <p>Загрузка задач...</p>
                        ) : projectTasks.length > 0 ? (
                            <ul className="tasks-list">
                                {projectTasks.map(task => (
                                    <li key={task.taskId} className="task-item">
                                        <strong>{task.taskName}</strong>
                                        <span className="task-status">Статус: {task.status || 'без статуса'}</span>
                                        <span className="task-priority">Приоритет: {task.priority || 'не указан'}</span>
                                        <div className="task-description">{task.description}</div>
                                    </li>
                                ))}
                            </ul>
                        ) : (
                            <p>В этом проекте пока нет задач</p>
                        )}
                        <button onClick={() => setIsTasksModalOpen(false)}>Закрыть</button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default ProjectPage;