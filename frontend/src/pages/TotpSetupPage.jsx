import { useEffect, useState } from "react";
import axios from "axios";
import { useLocation, useNavigate } from "react-router-dom";

function TotpSetupPage() {
    const [qrUrl, setQrUrl] = useState("");
    const [secret, setSecret] = useState("");

    const location = useLocation();
    const navigate = useNavigate();

    const email = location.state?.email;

    useEffect(() => {
        if (!email) {
            alert("이메일 정보가 없습니다.");
            navigate("/signup");
            return;
        }

        axios.get(`http://localhost:8080/api/auth/totp/setup`, { params: { email } })
            .then(res => {
                setQrUrl(res.data.qrUrl);
                setSecret(res.data.secret);
            })
            .catch(() => {
                alert("TOTP 설정 정보를 가져오지 못했습니다.");
                navigate("/signup");
            });
    }, [email, navigate]);

    return (
        <div style={{ padding: 20 }}>
            <h3>2단계 인증 설정</h3>
            <p>아래 QR 코드를 Google Authenticator로 스캔하세요.</p>
            {qrUrl && (
                <img
                    src={`https://api.qrserver.com/v1/create-qr-code/?data=${encodeURIComponent(qrUrl)}&size=200x200`}
                    alt="QR Code"
                />
            )}
            <p>또는 수동으로 등록: <b>{secret}</b></p>

            <br />
            <button onClick={() => navigate("/login")}>
                로그인 하러 가기
            </button>
        </div>
    );
}

export default TotpSetupPage;
