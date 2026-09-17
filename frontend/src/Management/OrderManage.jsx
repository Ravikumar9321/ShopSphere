import { useEffect, useState, useCallback } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../api/api";

function OrderManage() {
  const { sessionId } = useParams();
  const navigate = useNavigate();

  const [cartItems, setCartItems] = useState([]);
  const [originalCartTotal, setOriginalCartTotal] = useState(0);
  const [order, setOrder] = useState(null);

  const [cartLoading, setCartLoading] = useState(true);
  const [orderLoading, setOrderLoading] = useState(false);
  const [paymentLoading, setPaymentLoading] = useState(false);

  const [error, setError] = useState(null);

  const loadData = useCallback(async () => {
    if (!sessionId) return;
    setCartLoading(true);
    setError(null);
    try {
      const cartRes = await api.get(
        `http://localhost:8080/api/cartitem/sessionId/${sessionId}`,
      );
      const items = cartRes.data.data || cartRes.data || [];
      setCartItems(items);
    } catch (err) {
      setError(err.response?.data?.message || "❌ Failed to load cart");
    } finally {
      setCartLoading(false);
    }
  }, [sessionId]);

  const createOrder = useCallback(async () => {
    setOrderLoading(true);
    setError(null);
    try {
      const response = await api.post(
        `http://localhost:8080/api/order/${sessionId}`,
        {},
      );
      const orderData = response.data.data || response.data;
      setOrder(orderData);
      setOriginalCartTotal(orderData.totalAmount);
    } catch (err) {
      setError(err.response?.data?.message || "❌ Order creation failed");
    } finally {
      setOrderLoading(false);
    }
  }, [sessionId]);

  const completePayment = useCallback(async () => {
    setPaymentLoading(true);
    setError(null);
    try {
      await api.post(`http://localhost:8080/api/payment/order/${order.id}`, {});
      setOrder(null);
      setCartItems([]);
    } catch (err) {
      setError(err.response?.data?.message || "❌ Payment failed");
    } finally {
      setPaymentLoading(false);
    }
  }, [order?.id]);

  const getCartTotal = useCallback(() => {
    return cartItems.reduce(
      (total, item) => total + (item.product?.price * item.quantity || 0),
      0,
    );
  }, [cartItems]);

  useEffect(() => {
    loadData();
  }, [loadData]);

  if (cartLoading) {
    return (
      <div style={styles.loadingContainer}>
        <div style={styles.loadingSpinner}></div>
        <h2 style={styles.loadingText}>Loading cart...</h2>
      </div>
    );
  }

  return (
    <div style={styles.main}>
      <div style={styles.container}>
        {/* Header */}
        <div style={styles.header}>
          <h1 style={styles.title}>🧾 Order Checkout</h1>
          <p style={styles.sessionInfo}>
            Session: <code style={styles.code}>{sessionId}</code>
          </p>
        </div>

        {/* Cart Summary */}
        <div style={styles.section}>
          <h2 style={styles.sectionTitle}>
            🛒 Cart Summary ({cartItems.length} items)
          </h2>

          {error && <div style={styles.errorBanner}>❌ {error}</div>}

          <div style={styles.itemsList}>
            {cartItems.length === 0 ? (
              <p style={styles.emptyMessage}>
                No items in cart.{" "}
                <button
                  style={styles.addItemsLink}
                  onClick={() => navigate(`/cartitems/${sessionId}`)}
                >
                  Add items first →
                </button>
              </p>
            ) : (
              cartItems.map((item) => (
                <div key={item.id} style={styles.cartItem}>
                  <div style={styles.itemDetails}>
                    <h4 style={styles.itemName}>{item.product?.name}</h4>
                    <p style={styles.itemQty}>
                      Qty: {item.quantity} × ₹{item.product?.price}
                    </p>
                  </div>
                  <span style={styles.itemTotal}>
                    ₹{(item.product?.price * item.quantity).toLocaleString()}
                  </span>
                </div>
              ))
            )}
          </div>

          {cartItems.length > 0 && !order && (
            <>
              <div style={styles.totalRow}>
                Total: ₹{getCartTotal().toLocaleString()}
              </div>
              <button
                style={
                  orderLoading ? styles.primaryBtnDisabled : styles.primaryBtn
                }
                onClick={createOrder}
                disabled={orderLoading}
              >
                {orderLoading ? "Creating Order..." : "🚀 Place Order"}
              </button>
            </>
          )}
        </div>

        {/* Payment Section */}
        {order && (
          <div style={styles.paymentSectionContainer}>
            <h2 style={styles.sectionTitle}>💳 Complete Payment</h2>
            <div style={styles.paymentCard}>
              <h3 style={styles.orderPlacedTitle}>
                Order Placed Successfully! #{order.id}
              </h3>

              <div style={styles.orderTotal}>
                Total: ₹{order.totalAmount?.toLocaleString()}
              </div>

              <button
                style={styles.paymentBtn}
                onClick={completePayment}
                disabled={paymentLoading}
              >
                {paymentLoading
                  ? "Processing Payment..."
                  : "💳 Complete Payment"}
              </button>
            </div>
          </div>
        )}

        {/* Success */}
        {!order && cartItems.length === 0 && originalCartTotal > 0 && (
          <div style={styles.successSection}>
            <div style={styles.successMessage}>
              ✅ Order placed and payment completed successfully! <br />
              Total: ₹{originalCartTotal.toLocaleString()}
            </div>
          </div>
        )}

        {/* Footer */}
        <div style={styles.footer}>
          <button
            style={styles.backBtn}
            onClick={() => navigate(`/cartitems/${sessionId}`)}
          >
            ← Back to Cart
          </button>
          <button style={styles.homeBtn} onClick={() => navigate("/")}>
            🏠 Home
          </button>
        </div>
      </div>
    </div>
  );
}

export default OrderManage;

const styles = {
  main: {
    minHeight: "100vh",
    background:
      "linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%)",
    padding: "40px 20px",
    fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
  },
  container: {
    maxWidth: "1000px",
    margin: "0 auto",
  },
  header: {
    background:
      "linear-gradient(135deg, #ff6b6b 0%, #feca57 50%, #ff9ff3 100%)",
    color: "white",
    padding: "50px 40px",
    borderRadius: "30px",
    marginBottom: "40px",
    boxShadow: "0 25px 50px rgba(255, 107, 107, 0.4)",
    textAlign: "center",
  },
  title: {
    fontSize: "42px",
    fontWeight: "800",
    margin: 0,
    marginBottom: "16px",
    textShadow: "2px 2px 8px rgba(0,0,0,0.3)",
  },
  sessionInfo: {
    background: "rgba(255,255,255,0.25)",
    padding: "16px 24px",
    borderRadius: "30px",
    fontSize: "20px",
    fontWeight: "600",
    backdropFilter: "blur(10px)",
  },
  code: {
    background: "rgba(0,0,0,0.2)",
    padding: "4px 12px",
    borderRadius: "8px",
    fontFamily: "monospace",
  },
  section: {
    background: "rgba(255,255,255,0.95)",
    borderRadius: "30px",
    padding: "40px",
    marginBottom: "40px",
    boxShadow: "0 20px 60px rgba(0,0,0,0.15)",
    border: "1px solid rgba(255,255,255,0.3)",
  },
  sectionTitle: {
    fontSize: "28px",
    fontWeight: "700",
    background: "linear-gradient(135deg, #1e293b, #334155)",
    WebkitBackgroundClip: "text",
    WebkitTextFillColor: "transparent",
    backgroundClip: "text",
    marginBottom: "30px",
  },
  paymentSectionContainer: {
    background:
      "linear-gradient(135deg, rgba(16,185,129,0.1), rgba(5,150,105,0.1))",
    borderRadius: "35px",
    padding: "50px",
    marginBottom: "40px",
    border: "2px solid rgba(16,185,129,0.3)",
    boxShadow: "0 25px 60px rgba(16,185,129,0.2)",
  },
  paymentCard: {
    background:
      "linear-gradient(135deg, rgba(255,255,255,0.95), rgba(248,250,252,0.9))",
    borderRadius: "25px",
    padding: "50px",
    textAlign: "center",
    boxShadow: "0 30px 80px rgba(0,0,0,0.2)",
    border: "1px solid rgba(255,255,255,0.4)",
  },
  orderPlacedTitle: {
    fontSize: "32px",
    background: "linear-gradient(135deg, #059669, #10b981)",
    WebkitBackgroundClip: "text",
    WebkitTextFillColor: "transparent",
    backgroundClip: "text",
    marginBottom: "30px",
    fontWeight: "800",
  },
  orderTotal: {
    fontSize: "44px",
    background: "linear-gradient(135deg, #059669, #10b981)",
    WebkitBackgroundClip: "text",
    WebkitTextFillColor: "transparent",
    backgroundClip: "text",
    margin: "30px 0",
    fontWeight: "900",
  },
  itemsList: {
    maxHeight: "350px",
    overflowY: "auto",
    marginBottom: "30px",
    background: "rgba(255,255,255,0.7)",
    borderRadius: "20px",
    padding: "24px",
    border: "1px solid rgba(255,255,255,0.3)",
    backdropFilter: "blur(10px)",
  },
  cartItem: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    padding: "24px 0",
    borderBottom: "1px solid rgba(0,0,0,0.1)",
  },
  itemDetails: {
    flex: 1,
  },
  itemName: {
    fontSize: "20px",
    fontWeight: "700",
    color: "#1e293b",
    marginBottom: "6px",
  },
  itemQty: {
    color: "#64748b",
    fontSize: "18px",
    fontWeight: "500",
  },
  itemTotal: {
    fontSize: "22px",
    fontWeight: "800",
    background: "linear-gradient(135deg, #3b82f6, #1d4ed8)",
    WebkitBackgroundClip: "text",
    WebkitTextFillColor: "transparent",
    backgroundClip: "text",
  },
  totalRow: {
    textAlign: "right",
    padding: "30px 0",
    fontSize: "32px",
    background: "linear-gradient(135deg, #1e293b, #334155)",
    WebkitBackgroundClip: "text",
    WebkitTextFillColor: "transparent",
    backgroundClip: "text",
    borderTop: "3px solid rgba(59,130,246,0.3)",
    marginTop: "20px",
    fontWeight: "900",
  },
  primaryBtn: {
    width: "100%",
    padding: "24px",
    background: "linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)",
    color: "white",
    border: "none",
    borderRadius: "20px",
    fontSize: "22px",
    cursor: "pointer",
    fontWeight: "800",
    boxShadow: "0 15px 40px rgba(59,130,246,0.4)",
    transition: "all 0.3s ease",
  },
  primaryBtnDisabled: {
    width: "100%",
    padding: "24px",
    background: "linear-gradient(135deg, #9ca3af, #6b7280)",
    color: "white",
    border: "none",
    borderRadius: "20px",
    fontSize: "22px",
    cursor: "not-allowed",
  },
  paymentBtn: {
    width: "100%",
    padding: "28px",
    background:
      "linear-gradient(135deg, #10b981 0%, #059669 50%, #047857 100%)",
    color: "white",
    border: "none",
    borderRadius: "25px",
    fontSize: "26px",
    cursor: "pointer",
    fontWeight: "900",
    boxShadow: "0 20px 50px rgba(16,185,129,0.5)",
    transition: "all 0.3s ease",
  },
  successSection: {
    background:
      "linear-gradient(135deg, rgba(16,185,129,0.2), rgba(5,150,105,0.1))",
    borderRadius: "30px",
    padding: "50px",
    textAlign: "center",
    border: "2px solid rgba(16,185,129,0.4)",
  },
  successMessage: {
    background:
      "linear-gradient(135deg, rgba(255,255,255,0.8), rgba(248,250,252,0.9))",
    color: "#166534",
    padding: "30px",
    borderRadius: "25px",
    fontSize: "24px",
    fontWeight: "800",
    boxShadow: "0 15px 40px rgba(0,0,0,0.1)",
    border: "1px solid rgba(16,185,129,0.3)",
  },
  emptyMessage: {
    textAlign: "center",
    color: "rgba(255,255,255,0.8)",
    fontSize: "20px",
    padding: "80px 40px",
    fontWeight: "500",
  },
  footer: {
    display: "flex",
    gap: "24px",
    justifyContent: "center",
    marginTop: "60px",
    flexWrap: "wrap",
  },
  backBtn: {
    padding: "18px 40px",
    background: "linear-gradient(135deg, #6b7280 0%, #4b5563 100%)",
    color: "white",
    border: "none",
    borderRadius: "18px",
    cursor: "pointer",
    fontSize: "18px",
    fontWeight: "700",
    boxShadow: "0 10px 30px rgba(107,114,128,0.4)",
  },
  homeBtn: {
    padding: "18px 40px",
    background: "linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)",
    color: "white",
    border: "none",
    borderRadius: "18px",
    cursor: "pointer",
    fontSize: "18px",
    fontWeight: "700",
    boxShadow: "0 10px 30px rgba(59,130,246,0.4)",
  },
  addItemsLink: {
    background: "none",
    color: "#3b82f6",
    border: "none",
    fontSize: "20px",
    cursor: "pointer",
    textDecoration: "underline",
    fontWeight: "700",
  },
  loadingContainer: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
    height: "70vh",
    background: "rgba(255,255,255,0.1)",
    borderRadius: "25px",
    padding: "60px",
    backdropFilter: "blur(15px)",
  },
  loadingSpinner: {
    width: "80px",
    height: "80px",
    border: "8px solid rgba(255,255,255,0.2)",
    borderTop: "8px solid rgba(255,255,255,0.9)",
    borderRadius: "50%",
    animation: "spin 1s linear infinite",
  },
  loadingText: {
    marginTop: "30px",
    fontSize: "28px",
    color: "rgba(255,255,255,0.95)",
    fontWeight: "600",
  },
  errorBanner: {
    background:
      "linear-gradient(135deg, rgba(239,68,68,0.2), rgba(220,38,38,0.1))",
    color: "#dc2626",
    padding: "20px 24px",
    borderRadius: "18px",
    border: "1px solid rgba(239,68,68,0.3)",
    marginBottom: "30px",
    fontWeight: "600",
  },
};
