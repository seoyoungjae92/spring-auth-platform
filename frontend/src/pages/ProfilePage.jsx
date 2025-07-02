// src/pages/ProfilePage.jsx
function ProfilePage() {
    const email = localStorage.getItem("email");
    return <h2>프로필 페이지 - {email || "사용자 정보 없음"}</h2>;
}
export default ProfilePage;
