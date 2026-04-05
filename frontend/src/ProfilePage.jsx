import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { fetchWithAuth } from "./utils/auth";
import './ProfilePage.css';

const ProfilePage = () => {
  const navigate = useNavigate();
  const [isEditing, setIsEditing] = useState(false);
  const [profileData, setProfileData] = useState(null);
  const [formData, setFormData] = useState({});
  const token = localStorage.getItem("token");

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

  useEffect(() => {
    const storedData = localStorage.getItem('profileData');

    if (storedData) {
      const parsed = JSON.parse(storedData);
      setProfileData(parsed);
      setFormData(parsed.userDTO);
    } else if (token) {
      fetch('http://localhost:8081/profile', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
        }
      })
        .then(res => {
          if (!res.ok) throw new Error("Ошибка загрузки профиля");
          return res.json();
        })
        .then(data => {
          const newProfileData = { userDTO: data, tasks: data.tasks || [] };
          setProfileData(newProfileData);
          setFormData(data);
          localStorage.setItem('profileData', JSON.stringify(newProfileData));
        })
        .catch(err => console.error("Ошибка при загрузке:", err));
    }
  }, []);

  const handleEditToggle = () => {
    if (isEditing) {
      // Кнопка "Сохранить" — отправка на бэк
      handleSaveChanges();
    }
    setIsEditing(!isEditing);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSaveChanges = async () => {
    if (!token) {
      console.error("Token is not defined");
      return;
    }

    try {
      const response = await fetch('http://localhost:8081/profile/update', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        throw new Error('Ошибка при обновлении профиля');
      }

      const updatedUser = await response.json();
      const updatedProfileData = {
        ...profileData,
        userDTO: updatedUser,
      };
      setProfileData(updatedProfileData);
      localStorage.setItem('profileData', JSON.stringify(updatedProfileData));
    } catch (error) {
      console.error('Ошибка:', error);
    }
  };

  const handleExit = () => {
    localStorage.removeItem('profileData');
    localStorage.removeItem('token');
    navigate('/auth/login');
  };

  const handleLogoClick = () => {
    navigate('/home');
  };

  const handleDelete = () => {
    console.log('Профиль удален!');
    // Логика удаления профиля
  };

  if (!profileData) return <p>Загрузка профиля...</p>;

  const { tasks } = profileData;
  const user = formData;

  const profileImageSrc = user.profilePhoto
    ? `data:image/jpeg;base64,${user.profilePhoto}`
    : '/default_profile_image.jpg';

    const navigateToTaskPage = () => navigate("/task");
    const navigateToProjectPage = () => navigate("/project");
    const navigateToAnalytics = () => navigate("/analytics");

    return (
    <div className="base-page">
      {/* Навбар */}
      <div className="navbar">
        <button className="logo" onClick={handleLogoClick}></button>
        <div className="nav-buttons-group">
          <button className="nav-button" onClick={navigateToTaskPage}>Задачи</button>
          <button className="nav-button" onClick={navigateToProjectPage}>Проекты</button>
          <button className="nav-button" onClick={navigateToAnalytics}>Аналитика</button>
        </div>
      </div>

      <div className="main-content">
        {/* Боковая панель */}
        <div className="sidebar">
          <div className="sidebar-header">Мои недавние задачи:</div>
          <div className="item-card-group">
            {tasks?.length > 0 ? (
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
              <p>Нет задач</p>
            )}
          </div>
        </div>

        {/* Основной блок */}
        <div className="content-area">
          <div className="profile-info">
            <h2>Профиль пользователя</h2>

            <div className="user-info">
              <p><strong>Имя:</strong>
                {isEditing ? (
                  <input name="userName" value={user.userName} onChange={handleInputChange} />
                ) : user.userName}
              </p>
              <p><strong>Email:</strong>{user.email}</p>
              <p><strong>Роль:</strong> {user.role}</p>
              <p><strong>Соцсети:</strong>
                {isEditing ? (
                  <input name="socialLinks" value={user.socialLinks || ''} onChange={handleInputChange} />
                ) : user.socialLinks}
              </p>
              <p><strong>Дата рождения:</strong>
                {isEditing ? (
                    <input name="birthDate" value={user.birthDate ? new Date(user.birthDate).toISOString().slice(0, 10) : ''} onChange={handleInputChange} />
                    ) : user.birthDate}
              </p>
              <p><strong>Телефон:</strong>
                {isEditing ? (
                  <input name="phoneNumber" value={user.phoneNumber || ''} onChange={handleInputChange} />
                ) : user.phoneNumber}
              </p>
              <p><strong>Локация:</strong>
                {isEditing ? (
                  <input name="location" value={user.location || ''} onChange={handleInputChange} />
                ) : (user.location || 'Не указана')}
              </p>
              <p><strong>Описание профиля:</strong>
                {isEditing ? (
                  <textarea name="profileDescription" value={user.profileDescription || ''} onChange={handleInputChange} />
                ) : user.profileDescription}
              </p>

              <div className="profile-photo">
                <strong>Фото профиля:</strong>
                <img src="/foto_profile.jpeg" alt="Profile" className="profile-image" />
              </div>
            </div>

            <button className="edit-button" onClick={handleEditToggle}>
              {isEditing ? 'Сохранить изменения' : 'Редактировать'}
            </button>

            <button className="exit-button" onClick={handleExit}>
              Выйти
            </button>

            <button className="delete-button" onClick={handleDelete}>
                Удалить
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProfilePage;
