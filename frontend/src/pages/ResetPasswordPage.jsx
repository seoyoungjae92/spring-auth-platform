import { useState } from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

function ResetPasswordPage() {
    const navigate = useNavigate();
    const [email, setEmail] = useState("");

    const handleReset = async (e) => {
        e.preventDefault();
        try {
            await axios.post("http://localhost:8080/api/user/reset-password", { email });
            alert("임시 비밀번호가 이메일로 발송되었습니다. 로그인 페이지로 이동합니다.");
            navigate("/login");
        } catch (err) {
            const message = err.response?.data || "서버 오류";
            alert("오류: " + message);
        }
    };

    return (
        <form onSubmit={handleReset}>
            <input
                type="email"
                placeholder="이메일"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
            />
            <button type="submit">비밀번호 초기화</button>
        </form>
    );
}

export default ResetPasswordPage;
