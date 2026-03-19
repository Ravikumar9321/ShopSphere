import { useNavigate } from 'react-router-dom';

function HomePage() {
  const navigate = useNavigate();

  const goShop = () => navigate("/products");

  const styles = {
    main: {
      minHeight: "100vh",
      background: "linear-gradient(135deg, #1e3c72 0%, #2a5298 50%, #3b82f6 100%)",
      fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
      margin: 0,
      padding: 0
    },
    container: {
      maxWidth: "1200px",
      margin: "0 auto",
      padding: "0 20px"
    },
    header: {
      background: "linear-gradient(135deg, #ffffff 0%, #f8fafc 100%)",
      color: "#1e293b",
      padding: "1rem 0",
      boxShadow: "0 4px 20px rgba(0, 0, 0, 0.1)"
    },
    hero: {
      background: "linear-gradient(135deg, #6366f1 0%, #8b5cf6 50%, #ec4899 100%)",
      color: "white",
      padding: "120px 0 100px",
      textAlign: "center",
      minHeight: "70vh",
      display: "flex",
      alignItems: "center",
      position: "relative",
      overflow: "hidden"
    },
    title: {
      fontSize: "48px",
      fontWeight: "700",
      marginBottom: "1rem",
      lineHeight: "1.2",
      textShadow: "2px 2px 4px rgba(0,0,0,0.3)"
    },
    subtitle: {
      fontSize: "20px",
      marginBottom: "3rem",
      opacity: 0.95,
      maxWidth: "600px",
      margin: "0 auto 3rem auto"
    },
    shopBtn: {
      background: "linear-gradient(135deg, #10b981 0%, #059669 100%)",
      color: "white",
      border: "none",
      padding: "16px 40px",
      borderRadius: "12px",
      fontSize: "18px",
      fontWeight: "600",
      cursor: "pointer",
      transition: "all 0.3s ease",
      boxShadow: "0 8px 25px rgba(16, 185, 129, 0.4)",
      position: "relative",
      overflow: "hidden"
    }
  };

  return (
    <div style={styles.main}>
      {/* Header */}
      <header style={styles.header}>
        <div style={styles.container}>
          <h1 style={{fontSize: "28px", margin: 0, fontWeight: "700"}}>
            🛒 ShopSphere
          </h1>
          <p style={{margin: "0.25rem 0 0 0", fontSize: "16px"}}>
            Your Online Shopping Destination
          </p>
        </div>
      </header>

      {/* Hero Section */}
      <section style={styles.hero}>
        <div style={styles.container}>
          <h1 style={styles.title}>Welcome to ShopSphere</h1>
          <p style={styles.subtitle}>
            Discover amazing products with fast checkout and secure shopping
          </p>

          <button style={styles.shopBtn} onClick={goShop}>
            🚀 Start Shopping
          </button>
        </div>
      </section>
    </div>
  );
}

export default HomePage;
