import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Register.css";

const Register = () => {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(""); // состояние для ошибок
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        setError(""); // Очистка ошибки перед отправкой

        try {
            const response = await fetch("http://localhost:8081/auth/registration", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ username, email, password }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || `Ошибка: ${response.status}`);
            }

            const data = await response.json();
            console.log("Ответ от сервера:", data);

            // Если сервер возвращает токен, сохраняем его
            if (data.token) {
                localStorage.setItem("authToken", data.token);
            }

            // Перенаправляем пользователя на главную
            navigate("/auth/login");
        } catch (error) {
            console.error("Ошибка при запросе:", error);
            setError(error.message); // Показываем ошибку пользователю
        }
    };

    return (
        <div className="container">
            <h1>Регистрация</h1>
            {error && <p className="error-message">{error}</p>} {/* Отображение ошибки */}
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    className="input-field"
                    placeholder="Имя"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                />
                <input
                    type="email"
                    className="input-field"
                    placeholder="E-mail"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                <input
                    type="password"
                    className="input-field"
                    placeholder="Пароль"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <button type="submit" className="button" disabled={!email || !username || !password}>
                    Зарегистрироваться
                </button>
            </form>
            <div className="links">
                <a href="#" onClick={() => navigate("/auth/login")}>
                    Уже зарегистрированы?
                </a>
            </div>
        </div>
    );
};

export default Register;