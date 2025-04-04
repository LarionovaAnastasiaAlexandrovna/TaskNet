import React, { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";
import { fetchWithAuth } from "./utils/auth";
import './HomePage.css';

const HomePage = () => {
    const navigate = useNavigate();
    const [tasks, setTasks] = useState([]); // Сохраняем список задач
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchWithAuth("http://localhost:8081/api/tasks/recent") // Запрос к API
            .then(response => response.json())
            .then(data => setTasks(data))
            .catch(error => setError(error.message));
    }, []);

    const navigateToProfilePage = () => {
        navigate("/profile");
    };

    const navigateToProjectPage = () => {
        navigate("/project");
    };

    const navigateToTaskPage = () => {
        navigate("/task");
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
                        {error ? (
                            <p style={{ color: "red" }}>Ошибка: {error}</p>
                        ) : tasks.length > 0 ? (
                            tasks.map(task => (
                                <div className="item-card" key={task.id}>
                                    <div className="item-header">
                                        <div className="item-title">{task.title}</div>
                                        <div className="item-deadline">{task.deadline}</div>
                                        <div className="item-status">{task.status}</div>
                                    </div>
                                    <div className="item-body">{task.description}</div>
                                </div>
                            ))
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
