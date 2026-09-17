import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/api";

function ProductDetails() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [sessionId, setSessionId] = useState("");
  const [quantity, setQuantity] = useState(1);
  const [showPopup, setShowPopup] = useState(false);
  const [showSessionPopup, setShowSessionPopup] = useState(false);
  const [showRegisterPopup, setShowRegisterPopup] = useState(false);
  const [selectedProductId, setSelectedProductId] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState("");
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const loadProducts = async () => {
    try {
      setLoading(true);
      const res = await api.get("http://localhost:8080/api/product");
      setProducts(res.data.data || res.data);
      setError(null);
    } catch (error) {
      setError("❌ Failed to load products");
    } finally {
      setLoading(false);
    }
  };

  const searchCategory = async () => {
    if (!selectedCategory) {
      loadProducts();
      return;
    }
    try {
      setLoading(true);
      const response = await api.get(
        `http://localhost:8080/api/product/category/${selectedCategory}`,
      );
      setProducts(response.data.data || response.data);
      setError(null);
    } catch (error) {
      alert(error.response?.data?.message || error.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadProducts();
  }, []);

  const addToCart = (productId) => {
    setSelectedProductId(productId);
    setQuantity(1);
    setShowPopup(true);
  };
  const saveToCart = async () => {
    if (!sessionId.trim()) return alert("Enter Session ID!");
    try {
      setLoading(true);
      await api.post(`http://localhost:8080/api/cartitem/${sessionId}`, {
        quantity: parseInt(quantity),
        product: { id: selectedProductId },
      });
      setShowPopup(false);
      setQuantity(1);
      alert("✅ Added to cart!");
      await loadProducts();
    } catch (error) {
      alert(error.response?.data?.message || error.message);
      if (window.confirm("Do you want to register?")) {
        setShowRegisterPopup(true);
      }
    } finally {
      setLoading(false);
    }
  };

  const goToCart = async () => {
    try {
      await api.get(`http://localhost:8080/api/cart/sessionId/${sessionId}`);
      navigate(`/cartitems/${sessionId}`);
    } catch (error) {
      alert(error.response?.data?.message || error.message);
    }
  };

  const goRegister = async () => {
    try {
      setLoading(true);
      await api.post(`http://localhost:8080/api/cart`, {
        sessionId: sessionId,
      });
      alert("✅ Registered Successfully");
      setShowRegisterPopup(false);
      setShowSessionPopup(false);
    } catch (error) {
      alert(`❌ Error: ${error.response?.data?.message || error.message}`);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div style={styles.loading}>Loading...</div>;
  }

  return (
    <div style={styles.main}>
      <div style={styles.container}>
        <div style={styles.header}>
          <h1 style={styles.headerTitle}>🛒 Products</h1>
          <div style={styles.headerButtons}>
            <button style={styles.homeBtn} onClick={() => navigate("/home")}>
              ← Home
            </button>
            <button
              style={styles.cartBtn}
              onClick={() => setShowSessionPopup(true)}
            >
              🛒 View Cart
            </button>
          </div>
        </div>

        <div style={styles.searchSection}>
          <select
            value={selectedCategory}
            onChange={(e) => setSelectedCategory(e.target.value)}
            style={styles.select}
          >
            <option value="">All Products</option>
            <option value="Electronics">Electronics</option>
            <option value="Fashion">Fashion</option>
            <option value="Home & Living">Home & Living</option>
            <option value="Books">Books</option>
            <option value="Sports">Sports</option>
            <option value="Beauty">Beauty</option>
            <option value="Grocery">Grocery</option>
            <option value="Toys & Games">Toys & Games</option>
            <option value="Automotive">Automotive</option>
          </select>
          <button style={styles.searchBtn} onClick={searchCategory}>
            🔍 Search
          </button>
          <button style={styles.refreshBtn} onClick={loadProducts}>
            🔄 Refresh
          </button>
        </div>

        <div style={styles.grid}>
          {products.length === 0 ? (
            <p style={styles.noProducts}>{error || "No products available"}</p>
          ) : (
            products.map((product) => (
              <div key={product.id} style={styles.card}>
                <img
                  src={product.imageUrl || "https://picsum.photos/300/200"}
                  alt={product.name}
                  style={styles.image}
                />
                <h3 style={styles.productTitle}>{product.name}</h3>
                <p style={styles.description}>
                  {product.description?.slice(0, 60)}...
                </p>
                <div style={styles.priceStock}>
                  <span style={styles.price}>₹{product.price}</span>
                  <span style={styles.stock}>
                    Stock: {product.stockQuantity}
                  </span>
                </div>
                <button
                  style={styles.addBtn}
                  onClick={() => addToCart(product.id)}
                >
                  ➕ Add to Cart
                </button>
              </div>
            ))
          )}
        </div>
      </div>

      {/* Add to Cart Popup */}
      {showPopup && (
        <>
          <div style={styles.popupBg} onClick={() => setShowPopup(false)} />
          <div style={styles.popup}>
            <h3 style={styles.popupTitle}>📦 Add to Cart</h3>
            <input
              placeholder="Session ID (e.g. user123)"
              value={sessionId}
              onChange={(e) => setSessionId(e.target.value)}
              style={styles.input}
            />
            <input
              type="number"
              placeholder="Quantity"
              value={quantity}
              onChange={(e) => setQuantity(parseInt(e.target.value))}
              style={styles.input}
            />
            <div style={styles.popupButtons}>
              <button
                style={styles.cancelBtn}
                onClick={() => setShowPopup(false)}
              >
                Cancel
              </button>
              <button
                style={styles.addCartBtn}
                onClick={saveToCart}
                disabled={!sessionId.trim()}
              >
                Add to Cart
              </button>
            </div>
          </div>
        </>
      )}

      {/* View Cart Popup */}
      {showSessionPopup && (
        <>
          <div
            style={styles.popupBg}
            onClick={() => setShowSessionPopup(false)}
          />
          <div style={styles.popup}>
            <h3 style={styles.popupTitle}>🛒 Go to Cart</h3>
            <input
              placeholder="Enter your Session ID"
              value={sessionId}
              onChange={(e) => setSessionId(e.target.value)}
              style={styles.input}
            />
            <div style={styles.popupButtons}>
              <button
                style={styles.cancelBtn}
                onClick={() => setShowSessionPopup(false)}
              >
                Cancel
              </button>
              <button
                style={styles.goCartBtn}
                onClick={goToCart}
                disabled={!sessionId.trim()}
              >
                Go to Cart
              </button>
            </div>
          </div>
        </>
      )}

      {/* Register Popup */}
      {showRegisterPopup && (
        <>
          <div
            style={styles.popupBg}
            onClick={() => setShowRegisterPopup(false)}
          />
          <div style={styles.popup}>
            <h3 style={styles.popupTitle}>🛒 Register Session</h3>
            <input
              placeholder="Enter your Session ID"
              value={sessionId}
              onChange={(e) => setSessionId(e.target.value)}
              style={styles.input}
            />
            <div style={styles.popupButtons}>
              <button
                style={styles.cancelBtn}
                onClick={() => setShowRegisterPopup(false)}
              >
                Cancel
              </button>
              <button
                style={styles.registerBtn}
                onClick={goRegister}
                disabled={!sessionId?.trim()}
              >
                Register
              </button>
            </div>
          </div>
        </>
      )}
    </div>
  );
}

export default ProductDetails;

const styles = {
  main: {
    minHeight: "100vh",
    background:
      "linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%)",
    padding: "20px",
    fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
  },
  container: { maxWidth: "1200px", margin: "0 auto" },
  header: {
    background:
      "linear-gradient(135deg, #ff6b6b 0%, #feca57 50%, #ff9ff3 100%)",
    color: "white",
    padding: "25px",
    borderRadius: "20px",
    marginBottom: "25px",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    boxShadow: "0 10px 30px rgba(255, 107, 107, 0.3)",
  },
  headerTitle: {
    margin: 0,
    fontSize: "28px",
    fontWeight: "700",
    textShadow: "2px 2px 4px rgba(0,0,0,0.3)",
  },
  headerButtons: {
    display: "flex",
    gap: "15px",
  },
  searchSection: {
    marginBottom: "25px",
    padding: "20px",
    background:
      "linear-gradient(135deg, rgba(255,255,255,0.2), rgba(255,255,255,0.1))",
    borderRadius: "15px",
    backdropFilter: "blur(10px)",
    border: "1px solid rgba(255,255,255,0.2)",
  },
  select: {
    padding: "12px 16px",
    borderRadius: "10px",
    border: "2px solid rgba(255,255,255,0.3)",
    background: "rgba(255,255,255,0.9)",
    marginRight: "15px",
    fontSize: "16px",
    minWidth: "200px",
  },
  searchBtn: {
    background: "linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)",
    color: "white",
    border: "none",
    padding: "12px 24px",
    borderRadius: "10px",
    cursor: "pointer",
    fontWeight: "600",
    boxShadow: "0 4px 15px rgba(59, 130, 246, 0.3)",
  },
  refreshBtn: {
    background: "linear-gradient(135deg, #6b7280 0%, #4b5563 100%)",
    color: "white",
    border: "none",
    padding: "12px 24px",
    borderRadius: "10px",
    cursor: "pointer",
    fontWeight: "600",
    boxShadow: "0 4px 15px rgba(107, 114, 128, 0.3)",
  },
  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(300px, 1fr))",
    gap: "25px",
  },
  card: {
    background: "rgba(255,255,255,0.95)",
    borderRadius: "20px",
    padding: "25px",
    boxShadow: "0 15px 35px rgba(0,0,0,0.1)",
    textAlign: "center",
    border: "1px solid rgba(255,255,255,0.2)",
    transition: "transform 0.3s ease",
  },
  image: {
    width: "100%",
    height: "200px",
    objectFit: "cover",
    borderRadius: "15px",
    marginBottom: "20px",
    boxShadow: "0 8px 20px rgba(0,0,0,0.1)",
  },
  productTitle: {
    fontSize: "22px",
    fontWeight: "700",
    color: "#1e293b",
    margin: "0 0 10px 0",
  },
  description: {
    color: "#64748b",
    fontSize: "15px",
    marginBottom: "15px",
    lineHeight: "1.5",
  },
  priceStock: {
    display: "flex",
    justifyContent: "space-between",
    margin: "20px 0",
    alignItems: "center",
  },
  price: {
    fontSize: "26px",
    fontWeight: "800",
    background: "linear-gradient(135deg, #10b981, #059669)",
    WebkitBackgroundClip: "text",
    WebkitTextFillColor: "transparent",
    backgroundClip: "text",
  },
  stock: {
    color: "#f59e0b",
    fontWeight: "600",
    background: "rgba(245, 158, 11, 0.1)",
    padding: "4px 12px",
    borderRadius: "20px",
    fontSize: "14px",
  },
  addBtn: {
    background: "linear-gradient(135deg, #10b981 0%, #059669 100%)",
    color: "white",
    border: "none",
    padding: "15px 30px",
    borderRadius: "12px",
    fontSize: "16px",
    cursor: "pointer",
    width: "100%",
    fontWeight: "700",
    boxShadow: "0 8px 25px rgba(16, 185, 129, 0.4)",
    transition: "all 0.3s ease",
  },
  homeBtn: {
    background: "linear-gradient(135deg, #ef4444 0%, #dc2626 100%)",
    color: "white",
    border: "none",
    padding: "12px 24px",
    borderRadius: "10px",
    cursor: "pointer",
    fontWeight: "600",
    boxShadow: "0 4px 15px rgba(239, 68, 68, 0.3)",
  },
  cartBtn: {
    background: "linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)",
    color: "white",
    border: "none",
    padding: "12px 24px",
    borderRadius: "10px",
    cursor: "pointer",
    fontWeight: "600",
    boxShadow: "0 4px 15px rgba(59, 130, 246, 0.3)",
  },
  popupBg: {
    position: "fixed",
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    background: "rgba(0,0,0,0.6)",
    zIndex: 999,
    backdropFilter: "blur(5px)",
  },
  popup: {
    position: "fixed",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    background: "linear-gradient(135deg, #ffffff 0%, #f8fafc 100%)",
    padding: "35px",
    borderRadius: "20px",
    maxWidth: "450px",
    width: "90%",
    zIndex: 1000,
    textAlign: "center",
    boxShadow: "0 20px 60px rgba(0,0,0,0.3)",
    border: "1px solid rgba(255,255,255,0.2)",
  },
  popupTitle: {
    margin: "0 0 25px 0",
    fontSize: "24px",
    fontWeight: "700",
    color: "#1e293b",
  },
  input: {
    width: "100%",
    padding: "15px",
    border: "2px solid rgba(59, 130, 246, 0.2)",
    borderRadius: "12px",
    fontSize: "16px",
    marginBottom: "20px",
    boxSizing: "border-box",
    background: "rgba(255,255,255,0.9)",
    transition: "border-color 0.3s ease",
  },
  popupButtons: {
    display: "flex",
    gap: "20px",
    justifyContent: "center",
  },
  cancelBtn: {
    background: "linear-gradient(135deg, #6b7280 0%, #4b5563 100%)",
    color: "white",
    border: "none",
    padding: "14px 28px",
    borderRadius: "12px",
    cursor: "pointer",
    fontWeight: "600",
    boxShadow: "0 4px 15px rgba(107, 114, 128, 0.3)",
  },
  addCartBtn: {
    background: "linear-gradient(135deg, #10b981 0%, #059669 100%)",
    color: "white",
    border: "none",
    padding: "14px 28px",
    borderRadius: "12px",
    cursor: "pointer",
    fontWeight: "700",
    boxShadow: "0 8px 25px rgba(16, 185, 129, 0.4)",
  },
  goCartBtn: {
    background: "linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)",
    color: "white",
    border: "none",
    padding: "14px 28px",
    borderRadius: "12px",
    cursor: "pointer",
    fontWeight: "700",
    boxShadow: "0 8px 25px rgba(59, 130, 246, 0.4)",
  },
  registerBtn: {
    background: "linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%)",
    color: "white",
    border: "none",
    padding: "14px 28px",
    borderRadius: "12px",
    cursor: "pointer",
    fontWeight: "700",
    boxShadow: "0 8px 25px rgba(139, 92, 246, 0.4)",
  },
  noProducts: {
    textAlign: "center",
    gridColumn: "1/-1",
    color: "rgba(255,255,255,0.8)",
    padding: "60px",
    fontSize: "20px",
    fontWeight: "500",
  },
  loading: {
    textAlign: "center",
    padding: "100px",
    fontSize: "24px",
    color: "rgba(255,255,255,0.9)",
    background: "rgba(255,255,255,0.1)",
    borderRadius: "15px",
    margin: "50px auto",
    maxWidth: "400px",
  },
};
