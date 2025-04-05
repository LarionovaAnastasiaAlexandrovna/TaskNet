import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './ProfilePage.css';

const ProfilePage = () => {
    const navigate = useNavigate();
    const [profileData, setProfileData] = useState(null);

    useEffect(() => {
        const storedData = localStorage.getItem("profileData");
        if (storedData) {
            setProfileData(JSON.parse(storedData));
        }
    }, []);

    if (!profileData) {
        return <p>Загрузка профиля...</p>;
    }

    const { userDTO, tasks } = profileData;

    // Формируем правильный URL для изображения
    const profileImageSrc = userDTO.profilePhoto ? `data:image/jpeg;base64,${userDTO.profilePhoto}` : '/default_profile_image.jpg';

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
                        {tasks && tasks.length > 0 ? (
                            tasks.map((task, index) => (
                                <div key={index} className="item-card">
                                    <div className="item-header">
                                        <div className="item-title">{task.taskName}</div>
                                        <div className="item-deadline">{task.startDate}</div>
                                        <div className="item-status"></div>
                                    </div>
                                    <div className="item-body">{task.description}</div>
                                </div>
                            ))
                        ) : (
                            <p>Нет задач</p>
                        )}
                    </div>
                </div>

                <div className="content-area">
                    <div className="profile-info">
                        <h2>Профиль пользователя</h2>
                        <div className="user-info">
                            <p><strong>Имя:</strong> {userDTO.userName}</p>
                            <p><strong>Email:</strong> {userDTO.email}</p>
                            <p><strong>Роль:</strong> {userDTO.role}</p>
                            <p><strong>Соцсети:</strong> {userDTO.socialLinks}</p>
                            <p><strong>Дата рождения:</strong> {userDTO.birthDate.join('-')}</p>
                            <p><strong>Телефон:</strong> {userDTO.phoneNumber}</p>
                            <p><strong>Локация:</strong> {userDTO.location || 'Не указана'}</p>
                            <p><strong>Описание профиля:</strong> {userDTO.profileDescription}</p>
                            <div className="profile-photo">
                                <strong>Фото профиля:</strong>
                                <img src={profileImageSrc} alt="Profile" className="profile-image" />
                            </div>
                        </div>
                    </div>

                    <button className="exit-button" onClick={handleExit}>
                        Выйти
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ProfilePage;
