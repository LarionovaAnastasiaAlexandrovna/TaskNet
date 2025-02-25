import React from 'react';
import { useNavigate } from "react-router-dom";
import './TaskPage.css';
import './common.css';

const TaskPage = () => {
  
  const navigate = useNavigate();

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
              <div className="item-status"></div>
            </div>
            <div className="item-body">Task Description</div>
          </div>
          <div className="item-card">
            <div className="item-header">
              <div className="item-title">Task name2</div>
              <div className="item-deadline">Deadline 2</div>
              <div className="item-status"></div>
            </div>
            <div className="item-body">Task Description 2</div>
          </div>
        </div>
        </div>

        <div className="content-area">
        <div className="sidebar-header">Задача: Название задачи</div>
          <div className="item-info">
            <div className="item-description">
              Описание задачи: краткая информация о задаче.
            </div>
            <div className="item-deadline">Сроки: до 31 декабря 2025</div>
          </div>
          <div className="item-details">
            <h2>Детали задачи</h2>
            <div className="item-status">
              Статус: В процессе
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TaskPage;
