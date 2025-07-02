// src/pages/OAuthSuccessPage.jsx
import { useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";

function OAuthSuccessPage() {
    const navigate = useNavigate();
    const called = useRef(false); // 두 번 실행 방지용

    useEffect(() => {
        if (called.current) return;
        called.current = true;

        const params = new URLSearchParams(window.location.search);
        const accessToken = params.get("accessToken");
        const refreshToken = params.get("refreshToken");

        if (accessToken && refreshToken) {
            localStorage.setItem("accessToken", accessToken);
            localStorage.setItem("refreshToken", refreshToken);
            alert("소셜 로그인 성공!");
            navigate("/home");
        } else {
            alert("소셜 로그인 실패: 토큰 없음");
            navigate("/login");
        }
    }, [navigate]);

    return <p>로그인 처리 중...</p>;
}

export default OAuthSuccessPage;
