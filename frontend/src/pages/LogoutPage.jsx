// src/pages/LogoutPage.jsx
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function LogoutPage() {
    const navigate = useNavigate();

    useEffect(() => {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        alert("로그아웃 되었습니다.");
        navigate("/login");
    }, [navigate]);

    return null;
}

export default LogoutPage;
