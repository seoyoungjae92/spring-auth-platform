// src/App.js
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import LogoutPage from "./pages/LogoutPage";
import ChangePasswordPage from "./pages/ChangePasswordPage";
import DeleteAccountPage from "./pages/DeleteAccountPage";
import ResetPasswordPage from "./pages/ResetPasswordPage";
import OAuthSuccessPage from "./pages/OAuthSuccessPage";
import MfaVerifyPage from "./pages/MfaVerifyPage";
import TotpSetupPage from "./pages/TotpSetupPage";
import SignupPage from "./pages/SignupPage";
import HomePage from "./pages/HomePage";
import LoginHistoryPage from "./pages/LoginHistoryPage";
import PrivateRoute from "./components/PrivateRoute";

function App() {
    return (
        <Router>
            <Routes>
                {/* 공개 페이지 */}
                <Route path="/login" element={<LoginPage />} />
                <Route path="/signup" element={<SignupPage />} />
                <Route path="/reset-password" element={<ResetPasswordPage />} />
                <Route path="/oauth-success" element={<OAuthSuccessPage />} />
                <Route path="/mfa-verify" element={<MfaVerifyPage />} />
                <Route path="/totp-setup" element={<TotpSetupPage />} />

                {/* 보호된 페이지 */}
                <Route
                    path="/home"
                    element={
                        <PrivateRoute>
                            <HomePage />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/login-history"
                    element={
                        <PrivateRoute>
                            <LoginHistoryPage />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/change-password"
                    element={
                        <PrivateRoute>
                            <ChangePasswordPage />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/delete-account"
                    element={
                        <PrivateRoute>
                            <DeleteAccountPage />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/logout"
                    element={
                        <PrivateRoute>
                            <LogoutPage />
                        </PrivateRoute>
                    }
                />

                {/* 나머지는 로그인으로 리디렉션 */}
                <Route path="*" element={<LoginPage />} />
            </Routes>
        </Router>
    );
}

export default App;
