import React, { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";
import { fetchWithAuth } from "./utils/auth";
import './HomePage.css';
import './Common.css'

const HomePage = () => {
    const navigate = useNavigate();
    const [tasks, setTasks] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true); // <-- состояние загрузки

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

    const navigateToTaskPage = () => navigate("/task");
    const navigateToProjectPage = () => navigate("/project");

    const handleTaskClick = (id) => {
        navigate(`/task/${id}`);
    };

    return (
        <div className="base-page">
            <div className="navbar">
                <div className="logo"></div>
                <div className="nav-buttons-group">
                    <button className="nav-button">Отчеты</button>
                    <button className="nav-button" onClick={navigateToTaskPage}>Задачи</button>
                    <button className="nav-button" onClick={navigateToProjectPage}>Проекты</button>
                    <button className="nav-button" onClick={navigateToProfilePage}>Профиль</button>
                </div>
            </div>

            <div className="main-content">
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
                                    <div key={index} className="item-card">
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

                <div className="content-area">
                    Content Area
                </div>
            </div>
        </div>
    );
};

export default HomePage;
