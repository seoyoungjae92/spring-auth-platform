import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function SignupPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [isEmailAvailable, setIsEmailAvailable] = useState(false);
    const [emailChecked, setEmailChecked] = useState(false); // 중복 확인 여부
    const navigate = useNavigate();

    const checkEmail = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/auth/check-email", {
                params: { email },
            });
            alert(res.data); // "사용 가능한 이메일입니다."
            setIsEmailAvailable(true);
            setEmailChecked(true);
        } catch (err) {
            alert(err.response?.data || "서버 오류");
            setIsEmailAvailable(false);
            setEmailChecked(true);
        }
    };

    const handleSignup = async (e) => {
        e.preventDefault();

        if (!emailChecked) {
            alert("이메일 중복 확인을 먼저 해주세요.");
            return;
        }
        if (!isEmailAvailable) {
            alert("이미 사용 중인 이메일입니다.");
            return;
        }

        try {
            await axios.post("http://localhost:8080/api/auth/signup", { email, password });
            alert("회원가입 완료! 로그인 해주세요.");
            navigate("/login");
        } catch (err) {
            alert("회원가입 실패: " + (err.response?.data || "서버 오류"));
        }
    };

    return (
        <div style={{ padding: 20 }}>
            <h2>회원가입</h2>
            <form onSubmit={handleSignup}>
                <input
                    type="email"
                    value={email}
                    onChange={(e) => {
                        setEmail(e.target.value);
                        setEmailChecked(false); // 이메일 수정되면 중복 확인 다시 해야 함
                    }}
                    placeholder="이메일"
                    required
                />
                <button type="button" onClick={checkEmail}>
                    중복 확인
                </button>
                <br />
                <input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="비밀번호"
                    required
                />
                <br />
                <button type="submit">가입하기</button>
            </form>
        </div>
    );
}

export default SignupPage;
