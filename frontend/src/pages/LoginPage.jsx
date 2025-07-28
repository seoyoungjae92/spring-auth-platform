import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function LoginPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const res = await axios.post("http://localhost:8080/api/auth/login", {
                email,
                password,
            });

            if (res.data.accessToken) {
                localStorage.setItem("accessToken", res.data.accessToken);
                localStorage.setItem("refreshToken", res.data.refreshToken);
                window.alert("로그인 성공!");
                navigate("/home");
            } else if (res.data.mfaToken) {
                // MFA 대상자일 경우
                window.alert("2단계 인증이 필요합니다.");
                navigate("/mfa-verify", {
                    state: { mfaToken: res.data.mfaToken }
                });
            } else {
                throw new Error("서버 응답이 올바르지 않습니다.");
            }
        } catch (err) {
            setError("로그인 실패: " + (err.response?.data || "서버 오류"));
        }
    };

    const handleGoogleLogin = () => {
        window.location.href = "http://localhost:8080/oauth2/authorization/google";
    };

    return (
        <div style={{ padding: 20 }}>
            <h2>로그인</h2>

            <form onSubmit={handleLogin}>
                <div>
                    <input
                        type="email"
                        placeholder="이메일"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <input
                        type="password"
                        placeholder="비밀번호"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">로그인</button>
            </form>

            <hr />

            <div>
                <button onClick={handleGoogleLogin}>Google로 로그인</button>
            </div>

            <div style={{ marginTop: 10 }}>
                <p>
                    계정이 없으신가요?{" "}
                    <button onClick={() => navigate("/signup")} style={{ border: "none", background: "none", color: "blue", cursor: "pointer" }}>
                        회원가입
                    </button>
                </p>
                <p>
                    비밀번호를 잊으셨나요?{" "}
                    <button onClick={() => navigate("/reset-password")} style={{ border: "none", background: "none", color: "blue", cursor: "pointer" }}>
                        비밀번호 초기화
                    </button>
                </p>
            </div>

            {error && <p style={{ color: "red" }}>{error}</p>}
        </div>
    );
}

export default LoginPage;
