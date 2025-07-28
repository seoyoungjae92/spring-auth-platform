import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function LoginHistoryPage() {
    const [histories, setHistories] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("accessToken");
        if (!token) {
            alert("인증 정보가 없습니다. 다시 로그인해주세요.");
            navigate("/login");
            return;
        }

        axios.get("http://localhost:8080/api/admin/login-history", {
            headers: { Authorization: `Bearer ${token}` },
        })
            .then(res => setHistories(res.data))
            .catch(() => {
                alert("접근 권한이 없습니다.");
                navigate("/login");
            });
    }, [navigate]);

    return (
        <div style={{ padding: 20 }}>
            <h2>전체 로그인 기록</h2>
            <table border="1" cellPadding="5">
                <thead>
                <tr>
                    <th>유저 ID</th>
                    <th>이메일</th>
                    <th>로그인 시간</th>
                    <th>IP 주소</th>
                    <th>User-Agent</th>
                </tr>
                </thead>
                <tbody>
                {histories.map((item, idx) => (
                    <tr key={idx}>
                        <td>{item.userId}</td>
                        <td>{item.email}</td>
                        <td>{item.loginAt}</td>
                        <td>{item.ipAddress}</td>
                        <td>{item.userAgent}</td>
                    </tr>
                ))}
                </tbody>
            </table>
            <br />
            <button onClick={() => navigate("/home")}>뒤로가기</button>
        </div>
    );
}

export default LoginHistoryPage;
