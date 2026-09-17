import { useCallback, useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../api/api";

function OrderedDetails() {
  const { sessionId } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [orderDetails, setOrderDetails] = useState([]);
  const [error, setError] = useState(null);

  const viewOrderItem = (id) => navigate(`/orderitem/${id}`);
  const loadOrders = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await api.get(
        `http://localhost:8080/api/order/sessionId/${sessionId}`,
      );
      setOrderDetails(response.data.data || response.data);
    } catch (error) {
      setError(error.response.data?.message);
    } finally {
      setLoading(false);
    }
  }, [sessionId]);

  const confirmPayment = async (id) => {
    setLoading(true);
    try {
      await api.post(`http://localhost:8080/api/payment/order/${id}`, {});
      alert("✅ Payment done");
      loadOrders();
    } catch (error) {
      alert(
        "❌ Payment failed: " +
          (error.response?.data?.message || "Unknown error"),
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (sessionId) {
      loadOrders();
    }
  }, [sessionId, loadOrders]);
  if (loading) {
    return (
      <div style={styles.loadingContainer}>
        <div style={styles.loadingSpinner}></div>
        <h2 style={styles.loadingText}>Loading orders...</h2>
      </div>
    );
  }

  return (
    <div style={styles.main}>
      <div style={styles.container}>
        {/* Header */}
        <div style={styles.header}>
          <div style={styles.headerContent}>
            <h1 style={styles.title}>📋 Ordered Details</h1>
            <p style={styles.sessionInfo}>
              Session: <code style={styles.code}>{sessionId}</code>
            </p>
          </div>
          <button
            style={styles.backBtn}
            onClick={() => navigate(`/cartitems/${sessionId}`)}
          >
            ← Back to Cart
          </button>
        </div>

        {orderDetails.length === 0 ? (
          <div style={styles.emptyState}>
            <div style={styles.emptyIcon}>📭</div>
            <h3 style={styles.emptyTitle}>{error}</h3>
            <p style={styles.emptyText}>Your order history will appear here</p>
            <button
              style={styles.addOrderBtn}
              onClick={() => navigate(`/cartitems/${sessionId}`)}
            >
              Continue Shopping →
            </button>
          </div>
        ) : (
          <div style={styles.tableContainer}>
            <table style={styles.table}>
              <thead>
                <tr style={styles.tableHead}>
                  <th style={styles.tableHeader}>Order ID</th>
                  <th style={styles.tableHeader}>Date</th>
                  <th style={styles.tableHeader}>Status</th>
                  <th style={styles.tableHeader}>Total Amount</th>
                  <th style={styles.tableHeader}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {orderDetails.map((order) => (
                  <tr key={order.id} style={styles.tableRow}>
                    <td style={styles.tableCell}>
                      <span style={styles.orderId}>#{order.id}</span>
                    </td>
                    <td style={styles.tableCell}>{order.orderDate || "N/A"}</td>
                    <td style={styles.tableCell}>
                      <span style={styles.statusBadge(order.status)}>
                        {order.status || "Unknown"}
                      </span>
                    </td>
                    <td style={styles.tableCell}>
                      <span style={styles.amount}>
                        ₹{order.totalAmount?.toLocaleString() || 0}
                      </span>
                    </td>
                    <td style={styles.tableCell}>
                      {order.status === "PENDING" && (
                        <button
                          style={styles.paymentBtn}
                          onClick={() => confirmPayment(order.id)}
                          disabled={loading}
                        >
                          {loading ? "Processing..." : "💳 Complete Payment"}
                        </button>
                      )}
                    </td>
                    <td onClick={() => viewOrderItem(order.id)}>View</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}

const styles = {
  main: {
    minHeight: "100vh",
    background:
      "linear-gradient(135deg, #4facfe 0%, #00f2fe 50%, #43e97b 100%)",
    padding: "40px 20px",
    fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
  },
  container: {
    maxWidth: "1200px",
    margin: "0 auto",
  },
  header: {
    background:
      "linear-gradient(135deg, #ffecd2 0%, #fcb69f 50%, #ff8a80 100%)",
    color: "#2d3436",
    padding: "40px",
    borderRadius: "30px",
    marginBottom: "40px",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    boxShadow: "0 25px 60px rgba(255, 140, 128, 0.4)",
  },
  headerContent: {
    flex: 1,
  },
  title: {
    fontSize: "38px",
    fontWeight: "800",
    margin: "0 0 12px 0",
    lineHeight: "1.2",
  },
  sessionInfo: {
    fontSize: "18px",
    color: "#2d3436",
    fontWeight: "600",
    margin: 0,
  },
  code: {
    background: "rgba(0,0,0,0.1)",
    padding: "6px 12px",
    borderRadius: "8px",
    fontFamily: "monospace",
    fontWeight: "600",
  },
  backBtn: {
    background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
    color: "white",
    border: "none",
    padding: "16px 32px",
    borderRadius: "20px",
    fontSize: "16px",
    fontWeight: "700",
    cursor: "pointer",
    boxShadow: "0 10px 30px rgba(102, 126, 234, 0.4)",
  },
  tableContainer: {
    background: "rgba(255,255,255,0.95)",
    borderRadius: "30px",
    padding: "40px",
    boxShadow: "0 30px 80px rgba(0,0,0,0.2)",
    border: "1px solid rgba(255,255,255,0.3)",
  },
  table: {
    width: "100%",
    borderCollapse: "separate",
    borderSpacing: 0,
    background: "transparent",
  },
  tableHead: {
    background:
      "linear-gradient(135deg, rgba(255,255,255,0.8), rgba(248,250,252,0.9))",
    backdropFilter: "blur(15px)",
  },
  tableHeader: {
    padding: "20px",
    textAlign: "left",
    fontWeight: "700",
    fontSize: "16px",
    color: "#1e293b",
    borderBottom: "3px solid rgba(79, 172, 254, 0.3)",
  },
  tableRow: {
    transition: "all 0.3s ease",
  },
  tableRowHover: {
    background: "rgba(79, 172, 254, 0.1)",
    transform: "translateY(-2px)",
  },
  tableCell: {
    padding: "20px",
    borderBottom: "1px solid rgba(0,0,0,0.08)",
    verticalAlign: "middle",
  },
  orderId: {
    fontWeight: "700",
    color: "#1e293b",
    fontSize: "16px",
  },
  statusBadge: (status) => ({
    background:
      status === "PENDING"
        ? "linear-gradient(135deg, #f59e0b, #d97706)"
        : "linear-gradient(135deg, #10b981, #059669)",
    color: "white",
    padding: "8px 20px",
    borderRadius: "25px",
    fontSize: "14px",
    fontWeight: "700",
    boxShadow: "0 4px 15px rgba(245, 158, 11, 0.3)",
    display: "inline-block",
  }),
  amount: {
    fontSize: "18px",
    fontWeight: "800",
    background: "linear-gradient(135deg, #10b981, #059669)",
    WebkitBackgroundClip: "text",
    WebkitTextFillColor: "transparent",
    backgroundClip: "text",
  },
  paymentBtn: {
    background: "linear-gradient(135deg, #10b981 0%, #059669 100%)",
    color: "white",
    border: "none",
    padding: "12px 24px",
    borderRadius: "15px",
    cursor: "pointer",
    fontSize: "15px",
    fontWeight: "700",
    boxShadow: "0 8px 25px rgba(16, 185, 129, 0.4)",
    transition: "all 0.3s ease",
    minWidth: "140px",
  },
  emptyState: {
    textAlign: "center",
    padding: "100px 60px",
    background:
      "linear-gradient(135deg, rgba(255,255,255,0.2), rgba(255,255,255,0.1))",
    borderRadius: "35px",
    boxShadow: "0 30px 80px rgba(0,0,0,0.15)",
    backdropFilter: "blur(20px)",
    border: "1px solid rgba(255,255,255,0.3)",
  },
  emptyIcon: {
    fontSize: "80px",
    marginBottom: "30px",
    opacity: 0.8,
  },
  emptyTitle: {
    fontSize: "32px",
    color: "rgba(255,255,255,0.95)",
    margin: "0 0 16px 0",
    fontWeight: "700",
  },
  emptyText: {
    fontSize: "18px",
    color: "rgba(255,255,255,0.8)",
    marginBottom: "40px",
  },
  addOrderBtn: {
    background: "linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%)",
    color: "white",
    border: "none",
    padding: "20px 50px",
    borderRadius: "25px",
    fontSize: "18px",
    fontWeight: "800",
    cursor: "pointer",
    boxShadow: "0 15px 40px rgba(255, 107, 107, 0.4)",
  },
  loadingContainer: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
    height: "70vh",
    background: "rgba(255,255,255,0.15)",
    borderRadius: "30px",
    padding: "80px",
    backdropFilter: "blur(20px)",
  },
  loadingSpinner: {
    width: "80px",
    height: "80px",
    border: "8px solid rgba(255,255,255,0.3)",
    borderTop: "8px solid rgba(255,255,255,0.9)",
    borderRadius: "50%",
    animation: "spin 1s linear infinite",
    marginBottom: "30px",
  },
  loadingText: {
    fontSize: "28px",
    color: "rgba(255,255,255,0.95)",
    fontWeight: "600",
  },
};

export default OrderedDetails;
