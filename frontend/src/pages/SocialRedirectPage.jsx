// src/pages/SocialRedirectPage.jsx
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function SocialRedirectPage() {
    const navigate = useNavigate();

    useEffect(() => {
        const params = new URLSearchParams(window.location.search);
        const accessToken = params.get("accessToken");
        const refreshToken = params.get("refreshToken");

        if (accessToken && refreshToken) {
            localStorage.setItem("accessToken", accessToken);
            localStorage.setItem("refreshToken", refreshToken);
            alert("소셜 로그인 성공!");
            navigate("/profile");
        } else {
            alert("소셜 로그인 실패");
            navigate("/login");
        }
    }, []);

    return <p>소셜 로그인 처리 중...</p>;
}

export default SocialRedirectPage;
