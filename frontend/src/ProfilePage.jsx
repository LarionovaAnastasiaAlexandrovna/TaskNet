import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './ProfilePage.css';

const ProfilePage = () => {
  const navigate = useNavigate();
  const [isEditing, setIsEditing] = useState(false);
  const [profileData, setProfileData] = useState({
    name: 'Иван Иванов',
    email: 'ivan.ivanov@example.com',
    bio: 'Зарабатывает миллион в час',
    profileImage: '/foto_profile.jpeg',
  });

  const handleEditToggle = () => {
    setIsEditing(!isEditing);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProfileData({
      ...profileData,
      [name]: value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setIsEditing(false);
  };

  const handleDelete = () => {
    console.log('Профиль удален!');
    // Логика удаления профиля
  };

  const handleExit = () => {
    navigate('/auth/login');
  };

  const handleLogoClick = () => {
    navigate('/home');
  };

  return (
    <div className="base-page">
      <div className="navbar">
        <button className="logo" onClick={handleLogoClick}></button>
        <div className="nav-buttons-group">
          <button className="nav-button">Отчеты</button>
          <button className="nav-button">Задачи</button>
          <button className="nav-button">Проекты</button>
        </div>
      </div>

      <div className="main-content">
        <div className="sidebar">
          <div className="sidebar-header">Мои недавние задачи:</div>
          <div className="item-card-group">
            <div className="item-card">
              <div className="item-header">
                <div className="item-title">item Title</div>
                <div className="item-deadline">Deadline</div>
                <div className="item-status"></div>
              </div>
              <div className="item-body">item Description</div>
            </div>
            <div className="item-card">
              <div className="item-header">
                <div className="item-title">item Title 2</div>
                <div className="item-deadline">Deadline 2</div>
                <div className="item-status"></div>
              </div>
              <div className="item-body">Task Description 2</div>
            </div>
          </div>
        </div>

        <div className="content-area">
          <div className="profile-info">
            <div className="info-item">
              <span>Имя:</span>
              {isEditing ? (
                <input
                  type="text"
                  name="name"
                  value={profileData.name}
                  onChange={handleChange}
                />
              ) : (
                <span className="value">{profileData.name}</span>
              )}
            </div>
            <div className="info-item">
              <span>Email:</span>
              {isEditing ? (
                <input
                  type="email"
                  name="email"
                  value={profileData.email}
                  onChange={handleChange}
                />
              ) : (
                <span className="value">{profileData.email}</span>
              )}
            </div>
            <div className="info-item">
              <span>О себе:</span>
              {isEditing ? (
                <textarea
                  name="bio"
                  value={profileData.bio}
                  onChange={handleChange}
                />
              ) : (
                <span className="value">{profileData.bio}</span>
              )}
            </div>
            <div className="info-item">
              <span>Фото:</span>
              <img
                src={profileData.profileImage}
                alt="Profile"
                className="profile-image"
              />
            </div>
            <button className="edit-button" onClick={handleEditToggle}>
              {isEditing ? 'Сохранить изменения' : 'Редактировать'}
            </button>
            {/* {isEditing && (
              <button className="edit-button" onClick={handleSubmit}>
                Применить изменения
              </button>
            )} */}
          </div>

          <button className="delete-button" onClick={handleDelete}>
            Удалить
          </button>

          <button className="exit-button" onClick={handleExit}>
            Выйти
          </button>
        </div>
      </div>
    </div>
  );
};

export default ProfilePage;
