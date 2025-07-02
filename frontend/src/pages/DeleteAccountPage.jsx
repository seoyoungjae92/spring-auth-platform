// src/pages/DeleteAccountPage.jsx
import axios from "axios";

function DeleteAccountPage() {
    const handleDelete = async () => {
        if (!window.confirm("정말 탈퇴하시겠습니까?")) return;

        try {
            const token = localStorage.getItem("accessToken");
            await axios.delete("http://localhost:8080/api/user/me", {
                headers: { Authorization: `Bearer ${token}` },
            });
            localStorage.clear();
            alert("회원 탈퇴 완료");
            window.location.href = "/login";
        } catch (err) {
            alert("오류: " + err.response?.data || "서버 오류");
        }
    };

    return <button onClick={handleDelete}>회원 탈퇴</button>;
}

export default DeleteAccountPage;
