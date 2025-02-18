import React from 'react';
import { useNavigate } from "react-router-dom";  // Изменено с useHistory на useNavigate
import './HomePage.css';

const HomePage = () => {

  const navigate = useNavigate();

const navigateToProfilePage = () => {
  navigate("/profile");
};

  return (
    <div className="home-page">
      <div className="navbar">
        <div className="logo"></div>
        <div className="nav-buttons-group">
          <button className="nav-button">Отчеты</button>
          <button className="nav-button">Задачи</button>
          <button className="nav-button">Проекты</button>
          <button className="nav-button" onClick={navigateToProfilePage}>Профиль</button>
        </div>
      </div>

      <div className="main-content">
        <div className="sidebar">
          <div className="sidebar-header">Мои недавние задачи</div>
          <div className="task-card-group">
          <div className="task-card">
            <div className="task-header">
              <div className="task-title">Task Title</div>
              <div className="task-deadline">Deadline</div>
              <div className="task-status"></div>
            </div>
            <div className="task-body">Task Description</div>
          </div>

          <div className="task-card">
            <div className="task-header">
              <div className="task-title">Task Title 2</div>
              <div className="task-deadline">Deadline 2</div>
              <div className="task-status"></div>
            </div>
            <div className="task-body">Task Description 2</div>
          </div>
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
