import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";

function MfaVerifyPage() {
    const [code, setCode] = useState("");
    const navigate = useNavigate();
    const location = useLocation();

    const mfaToken = location.state?.mfaToken;

    const handleVerify = async (e) => {
        e.preventDefault();
        try {
            const res = await axios.post("http://localhost:8080/api/auth/mfa/verify", {
                mfaToken,
                code: parseInt(code),
            });
            localStorage.setItem("accessToken", res.data.accessToken);
            localStorage.setItem("refreshToken", res.data.refreshToken);
            navigate("/home");
        } catch (err) {
            alert("OTP 인증 실패: " + (err.response?.data || "서버 오류"));
        }
    };

    return (
        <form onSubmit={handleVerify}>
            <h2>2단계 인증</h2>
            <input
                type="text"
                placeholder="OTP 코드 (6자리)"
                value={code}
                onChange={(e) => setCode(e.target.value)}
                required
            />
            <button type="submit">인증</button>
        </form>
    );
}

export default MfaVerifyPage;
