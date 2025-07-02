import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function HomePage() {
    const navigate = useNavigate();
    const [user, setUser] = useState(null);

    useEffect(() => {
        const token = localStorage.getItem("accessToken");
        if (!token) {
            navigate("/login");
            return;
        }

        axios.get("http://localhost:8080/api/user/me", {
            headers: { Authorization: `Bearer ${token}` },
        })
            .then(res => setUser(res.data))
            .catch(() => {
                alert("인증 정보가 없습니다. 다시 로그인해주세요.");
                localStorage.clear();
                navigate("/login");
            });
    }, [navigate]);

    const logout = () => {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        alert("로그아웃 되었습니다");
        navigate("/login");
    };

    return (
        <div style={{ padding: 20 }}>
            <h2>홈페이지</h2>
            <p>로그인에 성공했습니다.</p>

            {user && !user.social && (
                <>
                    <button onClick={() => navigate("/change-password")}>비밀번호 변경</button>
                    <br />
                    <button onClick={() => navigate("/reset-password")}>비밀번호 초기화</button>
                    <br />
                </>
            )}

            <button onClick={() => navigate("/delete-account")}>회원 탈퇴</button>
            <br />
            <button onClick={logout}>로그아웃</button>
        </div>
    );
}

export default HomePage;
