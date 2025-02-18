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
                    className={`button-reset-password ${email ? "active" : ""}`}
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
        className={`button-reset-password ${(code && newPassword) ? "active" : ""}`}
        disabled={!code || !newPassword}
    >
        готово
    </button>
</>

            </form>
            <button className="link-button" onClick={() => navigate("/auth/login")}>
                вернуться на страницу входа
            </button>
        </div>
    );
};

export default PasswordReset;


//     const sendVerificationCode = () => {
//         fetch("http://localhost:8081/auth/send-reset-code", {
//             method: "POST",
//             headers: { "Content-Type": "application/json" },
//             body: JSON.stringify({ email }),
//         })
//             .then((response) => {
//                 if (!response.ok) throw new Error(`Ошибка: ${response.status}`);
//                 return response.json();
//             })
//             .then((data) => {
//                 console.log("Код подтверждения отправлен:", data);
//                 setCodeSent(true); // Переходим к следующему шагу
//             })
//             .catch((error) => console.error("Ошибка при отправке кода:", error));
//     };

//     const resetPassword = (event) => {
//         event.preventDefault();

//         fetch("http://localhost:8081/auth/reset-password", {
//             method: "POST",
//             headers: { "Content-Type": "application/json" },
//             body: JSON.stringify({ email, code, newPassword }),
//         })
//             .then((response) => {
//                 if (!response.ok) throw new Error(`Ошибка: ${response.status}`);
//                 return response.json();
//             })
//             .then((data) => {
//                 console.log("Успешный сброс пароля:", data);
//                 navigate("/auth/login");
//             })
//             .catch((error) => console.error("Ошибка при сбросе пароля:", error));
//     };

//     return (
//         <div className="container">
//             <h1>Сброс пароля</h1>
//             {!codeSent ? (
//                 <div className="step-1">
//                     <input
//                         type="email"
//                         className="input-field"
//                         placeholder="e-mail"
//                         value={email}
//                         onChange={(e) => setEmail(e.target.value)}
//                         required
//                     />
//                     <button
//                         type="button"
//                         className={`button-reset-password ${email ? "active" : ""}`}
//                         onClick={sendVerificationCode}
//                         disabled={!email}
//                     >
//                         отправить код подтверждения
//                     </button>
//                 </div>
//             ) : (
//                 <form onSubmit={resetPassword} className="step-2">
//                     <input
//                         type="text"
//                         className="input-field"
//                         placeholder="код подтверждения"
//                         value={code}
//                         onChange={(e) => setCode(e.target.value)}
//                         required
//                     />
//                     <input
//                         type="password"
//                         className="input-field"
//                         placeholder="новый пароль"
//                         value={newPassword}
//                         onChange={(e) => setNewPassword(e.target.value)}
//                         required
//                     />
//                     <button
//                         type="submit"
//                         className={`button-reset-password ${(code && newPassword) ? "active" : ""}`}
//                         disabled={!code || !newPassword}
//                     >
//                         готово
//                     </button>
//                 </form>
//             )}
//             <button className="link-button" onClick={() => navigate("/auth/login")}>
//                 вернуться на страницу входа
//             </button>
//         </div>
//     );
// };

// export default PasswordReset;
