import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function ChangePasswordPage() {
    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const navigate = useNavigate();

    const handleChangePassword = async (e) => {
        e.preventDefault();
        try {
            await axios.patch(
                "http://localhost:8080/api/user/password",
                { currentPassword, newPassword },
                {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
                    },
                }
            );

            alert("비밀번호가 변경되었습니다. 다시 로그인해주세요.");

            // 토큰 삭제
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");

            // 로그인 페이지로 이동
            navigate("/login");
        } catch (err) {
            alert("변경 실패: " + (err.response?.data || "서버 오류"));
        }
    };

    return (
        <div style={{ padding: 20 }}>
            <h2>비밀번호 변경</h2>
            <form onSubmit={handleChangePassword}>
                <input
                    type="password"
                    placeholder="현재 비밀번호"
                    value={currentPassword}
                    onChange={(e) => setCurrentPassword(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="새 비밀번호"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                    required
                />
                <button type="submit">변경</button>
            </form>
        </div>
    );
}

export default ChangePasswordPage;
