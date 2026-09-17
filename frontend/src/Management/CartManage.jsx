import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../api/api";

function CartManage() {
  const { sessionId } = useParams();
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const loadCart = async (sessionId) => {
    try {
      setLoading(true);
      const cartResponse = await api.get(
        `http://localhost:8080/api/cartitem/sessionId/${sessionId}`,
      );
      const items = cartResponse.data.data || cartResponse.data;
      setCartItems(items);
      setError(null);
    } catch (error) {
      setCartItems([]);
      setError(error.response?.data?.message || "❌ Failed to load cart items");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (sessionId) {
      loadCart(sessionId);
    }
  }, [sessionId]);

  const goProducts = () => {
    localStorage.removeItem("cartSession");
    navigate("/products");
  };

  const goOrder = async () => {
    try {
      setLoading(true);
      const response = await api.get(
        `http://localhost:8080/api/order/sessionId/${sessionId}`,
      );
      const existedOrder = response.data.data || response.data || [];
      const pendingOrders = existedOrder.filter((o) => o.status === "PENDING");

      if (pendingOrders.length >= 2) {
        if (window.confirm("First finish Pending Payment for the Orders")) {
          navigate(`/orderdetails/${sessionId}`);
        }
        return;
      }
      navigate(`/order/${sessionId}`);
    } catch (error) {
      if (error.response?.status === 404) {
        navigate(`/order/${sessionId}`);
        return;
      }
      setError(error.response?.data?.message || "❌ Error checking orders");
    } finally {
      setLoading(false);
    }
  };

  const goOrderdetails = () => navigate(`/orderdetails/${sessionId}`);

  const deleteProduct = async (id) => {
    try {
      if (window.confirm("Are you sure you want to delete?")) {
        await api.delete(`http://localhost:8080/api/cartitem/${id}`);
        await loadCart(sessionId);
      }
    } catch (error) {
      setError(error.response?.data?.message || "❌ Failed to delete item");
    }
  };

  const totalAmount = cartItems.reduce(
    (total, item) => total + (item.product?.price || 0) * (item.quantity || 1),
    0,
  );

  return (
    <div style={styles.main}>
      <div style={styles.container}>
        {/* Header */}
        <div style={styles.header}>
          <h1 style={styles.headerTitle}>
            🛒 My Cart (Session: <strong>{sessionId}</strong>)
          </h1>
          <div style={styles.headerButtons}>
            <button style={styles.continueShoppingBtn} onClick={goProducts}>
              ← Continue Shopping
            </button>
            <button style={styles.orderDetailsBtn} onClick={goOrderdetails}>
              Ordered Details
            </button>
          </div>
        </div>

        {loading ? (
          <div style={styles.loadingContainer}>
            <div style={styles.loading}>🔄 Loading cart...</div>
          </div>
        ) : cartItems.length === 0 ? (
          <div style={styles.emptyCart}>
            <div style={styles.emptyCartIcon}>🛒</div>
            <div style={styles.emptyCartTitle}>{error || "Cart is empty"}</div>
            <button style={styles.addProductsBtn} onClick={goProducts}>
              Add Products →
            </button>
          </div>
        ) : (
          <>
            <div style={styles.cartItemsContainer}>
              {cartItems.map((item) => {
                const imageUrl =
                  item.product?.imageUrl ||
                  "https://via.placeholder.com/80x80?text=Product";
                const totalPrice =
                  (item.product?.price || 0) * (item.quantity || 1);

                return (
                  <div key={item.id} style={styles.cartItem}>
                    <div style={styles.cartItemContent}>
                      <img
                        src={imageUrl}
                        alt={item.product?.name}
                        style={styles.productImage}
                        onError={(e) =>
                          (e.target.src =
                            "https://via.placeholder.com/80x80?text=No+Image")
                        }
                      />
                      <div style={styles.productInfo}>
                        <h3 style={styles.productName}>
                          {item.product?.name || "Unknown"}
                        </h3>
                        <p style={styles.productPrice}>
                          ₹{totalPrice.toLocaleString()}
                        </p>
                        <p style={styles.quantity}>Qty: {item.quantity || 1}</p>
                        {(item.paymentStatus === "PENDING" ||
                          item.order?.status === "PENDING") && (
                          <span style={styles.pendingBadge}>
                            ⏳ Pending Payment
                          </span>
                        )}
                        <button
                          style={styles.deleteBtn}
                          onClick={() => deleteProduct(item.id)}
                        >
                          🗑️ Delete
                        </button>
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>

            {/* Total and Checkout */}
            <div style={styles.totalSection}>
              <h3 style={styles.totalTitle}>
                Total: ₹{totalAmount.toLocaleString()}
              </h3>
              <button style={styles.proceedBtn} onClick={goOrder}>
                🚀 Proceed to Order
              </button>
            </div>
          </>
        )}
      </div>
    </div>
  );
}

export default CartManage;

const styles = {
  main: {
    minHeight: "100vh",
    background:
      "linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%)",
    padding: "30px 20px",
    fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
  },
  container: {
    maxWidth: "1200px",
    margin: "0 auto",
  },
  header: {
    background:
      "linear-gradient(135deg, #ff6b6b 0%, #feca57 50%, #ff9ff3 100%)",
    color: "white",
    padding: "30px",
    borderRadius: "25px",
    marginBottom: "30px",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    boxShadow: "0 15px 35px rgba(255, 107, 107, 0.4)",
  },
  headerTitle: {
    margin: 0,
    fontSize: "32px",
    fontWeight: "700",
    textShadow: "2px 2px 6px rgba(0,0,0,0.3)",
  },
  headerButtons: {
    display: "flex",
    gap: "20px",
  },
  continueShoppingBtn: {
    background: "linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)",
    color: "white",
    border: "none",
    padding: "15px 30px",
    borderRadius: "15px",
    fontSize: "16px",
    cursor: "pointer",
    fontWeight: "600",
    boxShadow: "0 8px 25px rgba(59, 130, 246, 0.4)",
    transition: "all 0.3s ease",
  },
  orderDetailsBtn: {
    background: "linear-gradient(135deg, #f59e0b 0%, #d97706 100%)",
    color: "white",
    border: "none",
    padding: "15px 30px",
    borderRadius: "15px",
    fontSize: "16px",
    cursor: "pointer",
    fontWeight: "600",
    boxShadow: "0 8px 25px rgba(245, 158, 11, 0.4)",
    transition: "all 0.3s ease",
  },
  loadingContainer: {
    padding: "100px 20px",
    textAlign: "center",
    background: "rgba(255,255,255,0.1)",
    borderRadius: "20px",
    backdropFilter: "blur(10px)",
  },
  loading: {
    fontSize: "28px",
    color: "rgba(255,255,255,0.9)",
    textShadow: "1px 1px 3px rgba(0,0,0,0.3)",
  },
  emptyCart: {
    textAlign: "center",
    padding: "100px 40px",
    background:
      "linear-gradient(135deg, rgba(255,255,255,0.2), rgba(255,255,255,0.1))",
    borderRadius: "25px",
    boxShadow: "0 20px 40px rgba(0,0,0,0.1)",
    backdropFilter: "blur(15px)",
    border: "1px solid rgba(255,255,255,0.2)",
  },
  emptyCartIcon: {
    fontSize: "64px",
    marginBottom: "20px",
    opacity: 0.8,
  },
  emptyCartTitle: {
    fontSize: "28px",
    color: "rgba(255,255,255,0.9)",
    marginBottom: "30px",
    fontWeight: "600",
  },
  addProductsBtn: {
    background: "linear-gradient(135deg, #10b981 0%, #059669 100%)",
    color: "white",
    border: "none",
    padding: "18px 40px",
    borderRadius: "20px",
    fontSize: "20px",
    cursor: "pointer",
    fontWeight: "700",
    boxShadow: "0 12px 30px rgba(16, 185, 129, 0.4)",
  },
  cartItemsContainer: {
    marginBottom: "40px",
  },
  cartItem: {
    background: "rgba(255,255,255,0.95)",
    padding: "30px",
    borderRadius: "25px",
    boxShadow: "0 20px 40px rgba(0,0,0,0.1)",
    marginBottom: "25px",
    border: "1px solid rgba(255,255,255,0.3)",
  },
  cartItemContent: {
    display: "flex",
    gap: "25px",
    alignItems: "center",
  },
  productImage: {
    width: "90px",
    height: "90px",
    objectFit: "cover",
    borderRadius: "15px",
    boxShadow: "0 10px 25px rgba(0,0,0,0.2)",
  },
  productInfo: {
    flex: 1,
  },
  productName: {
    margin: "0 0 12px 0",
    fontSize: "24px",
    color: "#1e293b",
    fontWeight: "700",
  },
  productPrice: {
    margin: "0 0 8px 0",
    fontSize: "22px",
    fontWeight: "800",
    background: "linear-gradient(135deg, #10b981, #059669)",
    WebkitBackgroundClip: "text",
    WebkitTextFillColor: "transparent",
    backgroundClip: "text",
  },
  quantity: {
    margin: "0 0 12px 0",
    color: "#64748b",
    fontSize: "18px",
    fontWeight: "500",
  },
  pendingBadge: {
    background: "linear-gradient(135deg, #f59e0b, #d97706)",
    color: "white",
    padding: "6px 16px",
    borderRadius: "25px",
    fontSize: "14px",
    fontWeight: "700",
    display: "inline-block",
    marginBottom: "15px",
    boxShadow: "0 4px 12px rgba(245, 158, 11, 0.3)",
  },
  deleteBtn: {
    background: "linear-gradient(135deg, #ef4444, #dc2626)",
    color: "white",
    border: "none",
    padding: "10px 20px",
    borderRadius: "10px",
    cursor: "pointer",
    fontSize: "15px",
    fontWeight: "600",
    boxShadow: "0 6px 20px rgba(239, 68, 68, 0.4)",
  },
  totalSection: {
    textAlign: "center",
    marginTop: "40px",
    padding: "40px",
    background:
      "linear-gradient(135deg, rgba(255,255,255,0.2), rgba(255,255,255,0.1))",
    borderRadius: "30px",
    boxShadow: "0 25px 50px rgba(0,0,0,0.15)",
    backdropFilter: "blur(20px)",
    border: "1px solid rgba(255,255,255,0.3)",
  },
  totalTitle: {
    color: "rgba(255,255,255,0.95)",
    marginBottom: "30px",
    fontSize: "32px",
    fontWeight: "800",
    textShadow: "2px 2px 8px rgba(0,0,0,0.3)",
  },
  proceedBtn: {
    background:
      "linear-gradient(135deg, #10b981 0%, #059669 50%, #047857 100%)",
    color: "white",
    padding: "22px 60px",
    borderRadius: "25px",
    border: "none",
    fontSize: "22px",
    cursor: "pointer",
    fontWeight: "800",
    boxShadow: "0 15px 40px rgba(16, 185, 129, 0.5)",
    transition: "all 0.3s ease",
  },
  noProducts: {
    textAlign: "center",
    gridColumn: "1/-1",
    color: "rgba(255,255,255,0.8)",
    padding: "60px",
    fontSize: "20px",
    fontWeight: "500",
  },
};
