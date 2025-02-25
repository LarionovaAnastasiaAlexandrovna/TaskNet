import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./ResetPassword.css";

const PasswordReset = () => {
    const [email, setEmail] = useState("");
    const [code, setCode] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [/*isCodeSent,*/ setIsCodeSent] = useState(false);
    const navigate = useNavigate();

    const handleSendCode = () => {
        console.log("Отправка кода на почту:", email);

        // Имитируем задержку отправки кода
        setTimeout(() => {
            console.log("Код подтверждения отправлен на почту:", email);
            setIsCodeSent(true);
        }, 1000);
    };

    const handleResetPassword = (event) => {
        event.preventDefault();

        console.log("Сброс пароля:", { email, code, newPassword });

        // Имитируем задержку сброса пароля
        setTimeout(() => {
            console.log("Пароль успешно сброшен для почты:", email);
            navigate("/auth/login");
        }, 1000);
    };

    return (
        <div className="container">
            <h1>Сброс пароля</h1>
            <form onSubmit={handleResetPassword}>
                <input
                    type="email"
                    className="input-field"
                    placeholder="e-mail"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                <button
                    type="button"
                    className={`button ${email ? "active" : ""}`}
                    disabled={!email}
                    onClick={handleSendCode}
                >
                    отправить код подтверждения
                </button>

                <>
    <input
        type="text"
        className="input-field"
        placeholder="код подтверждения"
        value={code}
        onChange={(e) => setCode(e.target.value)}
        required
    />
    <input
        type="password"
        className="input-field"
        placeholder="новый пароль"
        value={newPassword}
        onChange={(e) => setNewPassword(e.target.value)}
        required
    />
    <button
        type="submit"
        className={`button ${(code && newPassword) ? "active" : ""}`}
        disabled={!code || !newPassword}
    >
        готово
    </button>
</>
            </form>
            <div className="links">
                <a href="#" onClick={() => navigate("/auth/login")}>
                    вернуться на страницу входа
                </a>
            </div>
        </div>
    );
};

export default PasswordReset;