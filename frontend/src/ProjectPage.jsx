import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";
import './ProjectPage.css';
import './common.css';

const ProjectPage = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [newProject, setNewProject] = useState({
    projectName: '',
    description: '',
    startDate: '',
    endDate: ''
  });

  const [selectedProject, setSelectedProject] = useState(null); // новый выбранный проект
  const [projects, setProjects] = useState([]); // список проектов (пока локально

  const handleLogoClick = () => {
    navigate('/home');
  };

  const navigateToTaskPage = () => {
    navigate("/task");
  };

  const navigateToProfilePage = () => {
    navigate("/profile");
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

      if (!response.ok) {
        throw new Error("Ошибка при создании проекта");
      }

      const createdProject = await response.json();
      console.log("Проект создан:", createdProject);

      setProjects(prev => [...prev, createdProject]); // добавить в список проектов
      setSelectedProject(createdProject); // показать созданный проект

      setIsModalOpen(false);
      setNewProject({ projectName: '', description: '', startDate: '', endDate: '' });

    } catch (error) {
      console.error("Ошибка:", error);
      alert("Произошла ошибка при создании проекта.");
    }
  };

  return (
    <div className="base-page">
      {/* Навбар */}
      <div className="navbar">
        <div className="logo" onClick={handleLogoClick}></div>
        <div className="nav-buttons-group">
          <button className="nav-button">Отчеты</button>
          <button className="nav-button" onClick={navigateToTaskPage}>Задачи</button>
          <button className="nav-button">Проекты</button>
          <button className="nav-button" onClick={navigateToProfilePage}>Профиль</button>
        </div>
      </div>

      {/* Контент */}
      <div className="main-content">
        <div className="left-panel">
          <div className="sidebar">
            <div className="item-card-group">
              {projects.map((project, index) => (
                <div
                  key={index}
                  className="item-card"
                  onClick={() => setSelectedProject(project)}
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

          <button className="create-project-button" onClick={() => setIsModalOpen(true)}>
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
                  Статус: {selectedProject.projectStatus || "Не указан"}
                </div>
                <div className="project-tasks">
                  <h3>Задачи проекта:</h3>
                  <ul>
                    <li>Задачи пока нет</li>
                  </ul>
                </div>
              </div>
            </>
          ) : (
            <div className="sidebar-header">Выберите проект слева или создайте новый</div>
          )}
        </div>
      </div>

      {/* Модальное окно */}
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
    </div>
  );
};

export default ProjectPage;
