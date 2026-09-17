import { useState } from "react";
import api from "./api";
import { useNavigate } from "react-router-dom";

function AuthPage() {
  const navigate = useNavigate();

  // Login state
  const [loginEmail, setLoginEmail] = useState("");
  const [loginPassword, setLoginPassword] = useState("");

  // Register state
  const [regEmail, setRegEmail] = useState("");
  const [regPassword, setRegPassword] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await api.post("http://localhost:8080/api/auth/login", {
        email: loginEmail,
        password: loginPassword,
      });
      if (response.data.token) {
        localStorage.setItem("token", response.data.token);
        navigate("/home");
      } else {
        alert("Login failed");
      }
    } catch (error) {
      alert("Login failed — maybe not registered yet?");
    }
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      await api.post("http://localhost:8080/api/auth/register", {
        email: regEmail,
        password: regPassword,
      });
      alert("✅ Registration Successful");
      setRegEmail("");
      setRegPassword("");
    } catch (error) {
      alert(error.response?.data?.message || "❌ Registration Failed");
    }
  };

  return (
    <div style={styles.container}>
      {/* Login Section */}
      <div style={styles.box}>
        <h2 style={styles.title}>Login</h2>
        <form onSubmit={handleLogin}>
          <input
            type="email"
            placeholder="Email"
            value={loginEmail}
            onChange={(e) => setLoginEmail(e.target.value)}
            style={styles.input}
            required
          />
          <input
            type="password"
            placeholder="Password"
            value={loginPassword}
            onChange={(e) => setLoginPassword(e.target.value)}
            style={styles.input}
            required
          />
          <button type="submit" style={styles.button}>
            Login
          </button>
        </form>
      </div>

      {/* Registration Section */}
      <div style={styles.box}>
        <h2 style={styles.title}>Register</h2>
        <form onSubmit={handleRegister}>
          <input
            type="email"
            placeholder="Email"
            value={regEmail}
            onChange={(e) => setRegEmail(e.target.value)}
            style={styles.input}
            required
          />
          <input
            type="password"
            placeholder="Password"
            value={regPassword}
            onChange={(e) => setRegPassword(e.target.value)}
            style={styles.input}
            required
          />
          <button type="submit" style={styles.button}>
            Register
          </button>
        </form>
      </div>
    </div>
  );
}

const styles = {
  container: {
    display: "flex",
    flexWrap: "wrap",
    justifyContent: "center",
    alignItems: "center",
    gap: "40px",
    height: "100vh",
    background: "linear-gradient(135deg, #1e3c72 0%, #2a5298 100%)", // new gradient    fontFamily: "Arial, sans-serif",
    padding: "20px",
  },
  box: {
    background: "rgba(255, 255, 255, 0.2)", // glass effect
    backdropFilter: "blur(10px)",
    padding: "30px",
    borderRadius: "12px",
    boxShadow: "0 8px 32px rgba(0,0,0,0.2)",
    width: "320px",
    textAlign: "center",
  },
  title: {
    fontSize: "22px",
    fontWeight: "600",
    marginBottom: "15px",
    color: "#fff",
  },
  input: {
    width: "100%",
    padding: "12px",
    margin: "10px 0",
    border: "1px solid rgba(255,255,255,0.4)",
    borderRadius: "8px",
    fontSize: "14px",
    background: "rgba(255,255,255,0.3)",
    color: "#000",
  },
  button: {
    width: "100%",
    padding: "12px",
    marginTop: "12px",
    background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
    color: "#fff",
    border: "none",
    borderRadius: "8px",
    fontSize: "15px",
    fontWeight: "600",
    cursor: "pointer",
    transition: "transform 0.2s ease",
  },
};

export default AuthPage;
