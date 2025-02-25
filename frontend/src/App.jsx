import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Login from "./Login";
import Register from "./Register";
import ResetPassword from "./ResetPassword";
import HomePage from "./HomePage";
import ProfilePage from "./ProfilePage";
import ProjectPage from "./ProjectPage";
import TaskPage from "./TaskPage";

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/auth/login" element={<Login />} />
                <Route path="/auth/register" element={<Register />}/>
                <Route path="/auth/reset-password" element={<ResetPassword />} />
                <Route path="/home" element={<HomePage />} />
                <Route path="/profile" element={<ProfilePage />} />
                <Route path="/project" element={<ProjectPage />} />
                <Route path="/task" element={<TaskPage />} />
            </Routes>
        </Router>
    );
};

export default App;
