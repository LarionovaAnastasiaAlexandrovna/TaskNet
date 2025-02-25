import React, { useState } from "react";
import { useNavigate } from "react-router-dom";  // Для навигации
import "./Register.css";

const Register = () => {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();  // Используем useNavigate

    const handleSubmit = (event) => {
        event.preventDefault();
        console.log("Отправка данных:", { name, email, password });
        // Отправка данных на бэкенд (замени URL на свой)
        fetch("http://localhost:8081/auth/registration", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({name, email, password }),
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error(`Ошибка: ${response.status}`);
            }
            return response.json();
        })
        .then((data) => console.log("Ответ от сервера:", data))
        .catch((error) => console.error("Ошибка при запросе:", error));        
    };

    const navigateToLogin = () => {
        // Перенаправление на страницу входа
        navigate("/auth/login");
    };

    return (
        <div className="container">
            <h1>Регистрация</h1>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    className="input-field"
                    placeholder="имя"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                />
                <input
                    type="email"
                    className="input-field"
                    placeholder="e-mail"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                <input
                    type="password"
                    className="input-field"
                    placeholder="пароль"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <button type="submit" className="button" disabled={!email || !name || !password}>
                    зарегистрироваться
                </button>

            </form>
            <div className="links">
                <a href="#" onClick={navigateToLogin}>
                    уже зарегистрированы?
                </a>
            </div>
        </div>
    );
};

export default Register;
