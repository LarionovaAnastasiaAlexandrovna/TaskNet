import React, { useState, useEffect } from 'react';
import { fetchWithAuth } from './utils/auth';
import { useNavigate } from 'react-router-dom';
import {
    BarChart, Bar, LineChart, Line, XAxis, YAxis,
    CartesianGrid, Tooltip, Legend, ResponsiveContainer
} from 'recharts';
import './AnalyticsPage.css';

const AnalyticsPage = () => {
    const navigate = useNavigate();
    const [projects, setProjects] = useState([]);
    const [selectedProject, setSelectedProject] = useState('');
    const [selectedUser, setSelectedUser] = useState('');
    const [fromDate, setFromDate] = useState('');
    const [toDate, setToDate] = useState('');
    const [analyticsData, setAnalyticsData] = useState(null);
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    // Загрузка списка проектов
    useEffect(() => {
        fetchWithAuth('http://localhost:8081/project/all')
            .then(res => res.json())
            .then(data => setProjects(data))
            .catch(err => console.error(err));
    }, []);

    // Загрузка данных аналитики
    const loadAnalytics = async () => {
        if (!selectedProject) {
            setError("Выберите проект");
            return;
        }
        setLoading(true);
        setError(null);
        try {
            const params = new URLSearchParams({
                project_id: selectedProject,
                days: 30,
                ...(selectedUser && { user_id: selectedUser }),
                ...(fromDate && { from_date: fromDate }),
                ...(toDate && { to_date: toDate })
            });
            const response = await fetchWithAuth(`http://localhost:8000/api/v1/analytics/team-load?${params}`);
            if (!response.ok) throw new Error('Ошибка загрузки данных');
            const data = await response.json();
            setAnalyticsData(data);

            // Обновляем список пользователей для фильтра
            if (data.current_load && data.current_load.length > 0) {
                setUsers(data.current_load);
            }
        } catch (err) {
            console.error(err);
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    // При изменении фильтров автоматически обновляем данные
    useEffect(() => {
        if (selectedProject) {
            loadAnalytics();
        }
    }, [selectedProject, selectedUser, fromDate, toDate]);

    // Форматирование даты для отображения
    const formatDate = (dateString) => {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleDateString('ru-RU');
    };

    // Цвета для графиков
    const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8', '#82CA9D'];

    // Трансформация данных для stacked bar chart
    const getStackedData = () => {
        if (!analyticsData?.task_type_distribution) return [];
        const categories = [...new Set(analyticsData.task_type_distribution.map(d => d.category))];
        const usersMap = new Map();

        analyticsData.task_type_distribution.forEach(item => {
            if (!usersMap.has(item.user_name)) {
                usersMap.set(item.user_name, { user_name: item.user_name });
            }
            const userData = usersMap.get(item.user_name);
            userData[item.category] = (userData[item.category] || 0) + item.hours;
        });

        // Если выбран конкретный пользователь, показываем только его
        if (selectedUser) {
            const selectedUserName = users.find(u => u.user_id === parseInt(selectedUser))?.user_name;
            if (selectedUserName && usersMap.has(selectedUserName)) {
                return [usersMap.get(selectedUserName)];
            }
            return [];
        }
        return Array.from(usersMap.values());
    };

    // Фильтрация данных для графика эффективности
    const getEfficiencyData = () => {
        if (!analyticsData?.efficiency) return [];
        if (selectedUser) {
            return analyticsData.efficiency.filter(e => e.user_id === parseInt(selectedUser));
        }
        return analyticsData.efficiency;
    };

    // Фильтрация данных для соблюдения дедлайнов
    const getOnTimeData = () => {
        if (!analyticsData?.on_time_ratio) return [];
        if (selectedUser) {
            return analyticsData.on_time_ratio.filter(o => o.user_id === parseInt(selectedUser));
        }
        return analyticsData.on_time_ratio;
    };

    // Фильтрация текущей загрузки
    const getCurrentLoadData = () => {
        if (!analyticsData?.current_load) return [];
        if (selectedUser) {
            return analyticsData.current_load.filter(l => l.user_id === parseInt(selectedUser));
        }
        return analyticsData.current_load;
    };

    const handleLogoClick = () => {
        navigate('/home');
    };
    const navigateToTaskPage = () => navigate("/task");
    const navigateToProjectPage = () => navigate("/project");
    const navigateToProfile = () => navigate("/profile");

    return (
        <div className="base-page">
            <div className="navbar">
                <button className="logo" onClick={handleLogoClick}></button>
                <div className="nav-buttons-group">
                    <button className="nav-button" onClick={navigateToTaskPage}>Задачи</button>
                    <button className="nav-button" onClick={navigateToProjectPage}>Проекты</button>
                    <button className="nav-button" onClick={navigateToProfile}>Профиль</button>
                    <button className="nav-button active">Аналитика</button>
                </div>
            </div>

            <div className="main-content analytics-container">
                {/* Панель фильтров */}
                <div className="filters-panel">
                    <div className="filter-group">
                        <label>📁 Проект:</label>
                        <select
                            value={selectedProject}
                            onChange={(e) => setSelectedProject(e.target.value)}
                            className="filter-select"
                        >
                            <option value="">Выберите проект</option>
                            {projects.map(p => (
                                <option key={p.projectId} value={p.projectId}>
                                    {p.projectName}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="filter-group">
                        <label>👤 Сотрудник:</label>
                        <select
                            value={selectedUser}
                            onChange={(e) => setSelectedUser(e.target.value)}
                            className="filter-select"
                            disabled={!analyticsData?.current_load?.length}
                        >
                            <option value="">Все сотрудники</option>
                            {users.map(u => (
                                <option key={u.user_id} value={u.user_id}>
                                    {u.user_name}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="filter-group date-group">
                        <label>📅 Период:</label>
                        <input
                            type="date"
                            value={fromDate}
                            onChange={(e) => setFromDate(e.target.value)}
                            className="filter-date"
                        />
                        <span className="date-separator">—</span>
                        <input
                            type="date"
                            value={toDate}
                            onChange={(e) => setToDate(e.target.value)}
                            className="filter-date"
                        />
                    </div>

                    <button
                        className="filter-button"
                        onClick={loadAnalytics}
                        disabled={loading}
                    >
                        {loading ? '⏳ Загрузка...' : '🔄 Обновить'}
                    </button>
                </div>

                {/* Статус загрузки и ошибки */}
                {loading && <div className="loading-state">Загрузка аналитики...</div>}
                {error && <div className="error-state">❌ Ошибка: {error}</div>}

                {analyticsData && !loading && (
                    <div className="charts-grid">
                        {/* 1. Текущая загрузка */}
                        <div className="chart-card">
                            <h3>📊 Текущая загрузка (часы)</h3>
                            <p className="chart-subtitle">
                                {selectedUser ? 'Загрузка выбранного сотрудника' : 'Загрузка всех сотрудников'}
                            </p>
                            <ResponsiveContainer width="100%" height={300}>
                                <BarChart data={getCurrentLoadData()}>
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <XAxis dataKey="user_name" />
                                    <YAxis />
                                    <Tooltip formatter={(value) => `${value} ч`} />
                                    <Legend />
                                    <Bar dataKey="total_hours" fill="#8884d8" name="Часы" />
                                </BarChart>
                            </ResponsiveContainer>
                        </div>

                        {/* 2. Прогноз загрузки */}
                        <div className="chart-card">
                            <h3>📈 Прогноз загрузки на 30 дней на весь проект</h3>
                            <p className="chart-subtitle">Сумма estimated_hours по дедлайнам</p>
                            <ResponsiveContainer width="100%" height={300}>
                                <LineChart data={analyticsData.forecast_load}>
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <XAxis dataKey="date" tickFormatter={formatDate} />
                                    <YAxis />
                                    <Tooltip formatter={(value) => `${value} ч`} labelFormatter={formatDate} />
                                    <Legend />
                                    <Line type="monotone" dataKey="total_hours" stroke="#82ca9d" name="Часы" />
                                </LineChart>
                            </ResponsiveContainer>
                        </div>

                        {/* 3. Эффективность */}
                        <div className="chart-card">
                            <h3>⚡ Эффективность (actual/estimated)</h3>
                            <p className="chart-subtitle">
                                {selectedUser ? 'Значение 1 = точно в срок, <1 быстрее, >1 медленнее' : 'Средняя эффективность по сотрудникам'}
                            </p>
                            <ResponsiveContainer width="100%" height={300}>
                                <BarChart data={getEfficiencyData()}>
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <XAxis dataKey="user_name" />
                                    <YAxis domain={[0, 2.5]} />
                                    <Tooltip formatter={(value) => value?.toFixed(2)} />
                                    <Legend />
                                    <Bar dataKey="avg_efficiency" fill="#ffc658" name="Эффективность" />
                                </BarChart>
                            </ResponsiveContainer>
                        </div>

                        {/* 4. Соблюдение дедлайнов */}
                        <div className="chart-card">
                            <h3>⏰ Соблюдение дедлайнов</h3>
                            <p className="chart-subtitle">Процент задач, сданных вовремя</p>
                            <ResponsiveContainer width="100%" height={300}>
                                <BarChart data={getOnTimeData()}>
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <XAxis dataKey="user_name" />
                                    <YAxis domain={[0, 1]} tickFormatter={(tick) => `${tick * 100}%`} />
                                    <Tooltip formatter={(value) => `${(value * 100).toFixed(0)}%`} />
                                    <Legend />
                                    <Bar dataKey="ratio" fill="#8884d8" name="Вовремя" />
                                </BarChart>
                            </ResponsiveContainer>
                        </div>

                        {/* 5. Распределение типов задач */}
                        <div className="chart-card wide-chart">
                            <h3>📋 Распределение типов задач по сотрудникам в часах</h3>
                            <p className="chart-subtitle">
                                {selectedUser ? 'Распределение задач по категориям' : 'Stacked bar chart по категориям'}
                            </p>
                            <ResponsiveContainer width="100%" height={400}>
                                <BarChart data={getStackedData()}>
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <XAxis dataKey="user_name" />
                                    <YAxis />
                                    <Tooltip />
                                    <Legend />
                                    {[...new Set(analyticsData.task_type_distribution?.map(d => d.category) || [])].map((cat, idx) => (
                                        <Bar key={cat} dataKey={cat} stackId="a" fill={COLORS[idx % COLORS.length]} />
                                    ))}
                                </BarChart>
                            </ResponsiveContainer>
                        </div>

                        {/* 6. Динамика задач */}
                        <div className="chart-card wide-chart">
                            <h3>🔄 Динамика создания и закрытия задач</h3>
                            <p className="chart-subtitle">Количество созданных и закрытых задач по дням</p>
                            <ResponsiveContainer width="100%" height={350}>
                                <LineChart data={analyticsData.task_dynamics}>
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <XAxis dataKey="date" tickFormatter={formatDate} />
                                    <YAxis />
                                    <Tooltip labelFormatter={formatDate} />
                                    <Legend />
                                    <Line type="monotone" dataKey="created" stroke="#8884d8" name="Создано" />
                                    <Line type="monotone" dataKey="closed" stroke="#82ca9d" name="Закрыто" />
                                </LineChart>
                            </ResponsiveContainer>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default AnalyticsPage;