import React from 'react';
import { useNavigate } from "react-router-dom";
import './HomePage.css';

const HomePage = () => {

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
  return (
    <div className="base-page">
      <div className="navbar">
        <div className="logo"></div>
        <div className="nav-buttons-group">
          <button className="nav-button">Отчеты</button>
          <button className="nav-button"onClick={navigateToTaskPage}>Задачи</button>
          <button className="nav-button"onClick={navigateToProjectPage}>Проекты</button>
          <button className="nav-button" onClick={navigateToProfilePage}>Профиль</button>
        </div>
      </div>

      <div className="main-content">
        <div className="sidebar">
          <div className="sidebar-header">Мои недавние задачи:</div>
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
              <div className="item-title">Task Title 2</div>
              <div className="item-deadline">Deadline 2</div>
              <div className="item-status"></div>
            </div>
            <div className="item-body">Task Description 2</div>
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
