import React from 'react';
import { useNavigate } from "react-router-dom";
import './ProjectPage.css';
import './common.css';

const ProjectPage = () => {
  
  const navigate = useNavigate();

  const navigateToProfilePage = () => {
    navigate("/profile");
  };

  const handleLogoClick = () => {
    navigate('/home');
  };

  const navigateToTaskPage = () => {
    navigate("/task");
  };

  return (
    <div className="base-page">
      <div className="navbar">
        <div className="logo" onClick={handleLogoClick}></div>
        <div className="nav-buttons-group">
          <button className="nav-button">Отчеты</button>
          <button className="nav-button" onClick={navigateToTaskPage}>Задачи</button>
          <button className="nav-button">Проекты</button>
          <button className="nav-button" onClick={navigateToProfilePage}>Профиль</button>
        </div>
      </div>

      <div className="main-content">
        <div className="sidebar">
          <div className="item-card-group">
          <div className="item-card">
            <div className="item-header">
              <div className="item-title">Project Title</div>
              <div className="item-deadline">Deadline</div>
              <div className="item-status"></div>
            </div>
            <div className="item-body">Project Description</div>
          </div>
          <div className="item-card">
            <div className="item-header">
              <div className="item-title">Project name2</div>
              <div className="item-deadline">Deadline 2</div>
              <div className="item-status"></div>
            </div>
            <div className="item-body">Project Description 2</div>
          </div>
        </div>
        </div>

        <div className="content-area">
        <div className="sidebar-header">Проект: Название проекта</div>
          <div className="project-info">
            <div className="project-description">
              Описание проекта: краткая информация о проекте.
            </div>
            <div className="project-deadline">Сроки: до 31 декабря 2025</div>
          </div>
          <div className="project-details">
            <h2>Детали проекта</h2>
            <div className="project-status">
              Статус: В процессе
            </div>
            <div className="project-tasks">
              <h3>Задачи проекта:</h3>
              <ul>
                <li>Задача 1</li>
                <li>Задача 2</li>
                <li>Задача 3</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProjectPage;
