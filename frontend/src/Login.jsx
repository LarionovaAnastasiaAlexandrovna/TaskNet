import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Login.css";

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const navigate = useNavigate();

    const handleSubmit = (event) => {
        event.preventDefault();
        console.log("Отправка данных:", { email, password });

        fetch("http://localhost:8081/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ email, password }),
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error(`Ошибка: ${response.status}`);
            }
            return response.json();
        })
        .then((data) => {
            console.log("Ответ от сервера:", data);
            // Сохраняем токен в localStorage
            localStorage.setItem("token", data.token);
            navigate("/home"); // После успешного входа
        })
        .catch((error) => console.error("Ошибка при запросе:", error));

    };

    const navigateToRegister = () => {
        // Перенаправление на страницу регистрации
        navigate("/auth/register");  // Используем navigate вместо history.push
    };

    const navigateToResetPassword = () => {
        navigate("/auth/reset-password");
    };

    return (
        <div className="container">
            <h1>Вход</h1>
            <form onSubmit={handleSubmit}>
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
                <button type="submit" className="button" disabled={!email || !password}>
                    войти
                </button>
            </form>

            {errorMessage && <p className="error-message">{errorMessage}</p>} {/* Показываем ошибку */}

            <div className="links">
                <a href="#" onClick={navigateToResetPassword}>
                    забыли пароль?
                </a>
                <a href="#" onClick={navigateToRegister}>
                    ещё не зарегистрированы?
                </a>
            </div>
        </div>
    );
};

export default Login;
