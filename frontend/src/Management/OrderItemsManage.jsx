import axios from "axios";
import { useState, useEffect, useCallback } from "react";
import { useParams } from "react-router-dom";

function OrderitemManage() {
    const { orderid } = useParams();
    const [order, setOrder] = useState(null);
    const [payment, setPayment] = useState(null);  
    const [orderItems, setOrderItems] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const loadOrder = useCallback(async () => {
        setError(null);
        setLoading(true);
        try {
            const orderRes = await axios.get(`http://localhost:8080/api/order/${orderid}`);
            const orderData = orderRes.data.data || orderRes.data;
            setOrder(orderData);

            const [orderItemRes, paymentRes] = await Promise.all([
                axios.get(`http://localhost:8080/api/orderitem/order/${orderid}`),  
                axios.get(`http://localhost:8080/api/payment/order/${orderid}`)
            ]);
            setPayment(paymentRes.data.data || paymentRes.data || null);
            setOrderItems(orderItemRes.data.data || orderItemRes.data || []);
        } catch (err) {
            setError(err.response?.data?.message || err.message || 'Failed to load order');
        } finally {
            setLoading(false);
        }
    },[orderid]);

    useEffect(() => {
        if (orderid) loadOrder();
    }, [orderid,loadOrder]);

  

    if (loading) return (
        <div style={styles.loadingBox}>
            <div style={{
                width: '50px',
                height: '50px',
                border: '4px solid rgba(255,255,255,0.3)',
                borderTop: '4px solid white',
                borderRadius: '50%',
                marginBottom: '20px'
            }} />
            <p style={{ fontSize: '18px', fontWeight: '500' }}>Loading order details...</p>
        </div>
    );
    
    if (error) return (
        <div style={styles.errorBox}>
            <div style={{ fontSize: '48px', marginBottom: '20px' }}>⚠️</div>
            <p style={{ fontSize: '18px', fontWeight: '500' }}>Error: {error}</p>
        </div>
    );

    return (
        <div style={styles.container}>
            {/* Order Header */}
            <div style={styles.header}>
                <div style={styles.headerContent}>
                    <div>
                        <h1 style={styles.orderTitle}>Order #{order?.id}</h1>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '15px', marginTop: '10px' }}>
                            <span style={styles.statusBadge}>{order?.status || 'Loading...'}</span>
                            {order?.orderDate && (
                                <span style={{ color: 'rgba(255,255,255,0.9)', fontSize: '16px' }}>
                                    {new Date(order.orderDate).toLocaleDateString('en-IN')}
                                </span>
                            )}
                        </div>
                    </div>
                    <div style={styles.totalSection}>
                        <div style={{ color: 'rgba(255,255,255,0.8)', fontSize: '16px' }}>Total Amount</div>
                        <div style={styles.totalAmount}>₹{order?.totalAmount?.toLocaleString()}</div>
                    </div>
                </div>
            </div>

            {/* Items Card */}
            <div style={styles.card}>
                <div style={styles.cardHeader}>
                    <h2 style={{ ...styles.cardTitle, display: 'flex', alignItems: 'center', gap: '10px' }}>
                        🛍️ Order Items ({orderItems.length})
                    </h2>
                </div>
                <div style={styles.itemList}>
                    {orderItems.length > 0 ? (
                        orderItems.map(item => (
                            <div key={item.id} style={styles.itemRow}>
                                <div>
                                    <div style={styles.itemName}>{item.product?.name || 'N/A'}</div>
                                    <div style={styles.itemDetails}>
                                        <span>Qty: {item.quantity}</span>
                                        <span>₹{item.price?.toLocaleString()}</span>
                                    </div>
                                </div>
                                <div style={styles.itemTotal}>
                                    ₹{(item.quantity * (item.price || 0))?.toLocaleString()}
                                </div>
                            </div>
                        ))
                    ) : (
                        <div style={{ textAlign: 'center', padding: '40px', color: 'rgba(255,255,255,0.7)' }}>
                            <div style={{ fontSize: '48px', marginBottom: '15px' }}>📦</div>
                            <p style={{ fontSize: '16px' }}>No items in this order</p>
                        </div>
                    )}
                </div>
            </div>

            {/* Payment Card */}
            {payment && (
                <div style={styles.card}>
                    <div style={styles.cardHeader}>
                        <h2 style={{ ...styles.cardTitle, display: 'flex', alignItems: 'center', gap: '10px' }}>
                            💳 Payment Details
                        </h2>
                    </div>
                    <div style={styles.itemList}>
                        <div style={styles.paymentRow}>
                            <span style={{ color: 'rgba(255,255,255,0.9)', fontWeight: '600' }}>Status:</span>
                            <span style={styles.statusBadge}>{payment.status}</span>
                        </div>
                        <div style={styles.paymentRow}>
                            <span style={{ color: 'rgba(255,255,255,0.9)', fontWeight: '600' }}>Amount:</span>
                            <span style={{
                                fontSize: '22px',
                                fontWeight: 'bold',
                                color: '#00d4aa'
                            }}>₹{payment.amount?.toLocaleString()}</span>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
  
    const styles = {
        container: {
            minHeight: '100vh',
            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
            padding: '20px',
            fontFamily: 'Arial, sans-serif',
            color: 'white'
        },
        header: {
            background: 'linear-gradient(45deg, #ff6b6b, #feca57)',
            borderRadius: '15px',
            padding: '25px',
            marginBottom: '20px',
            boxShadow: '0 10px 30px rgba(0,0,0,0.3)'
        },
        headerContent: {
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            flexWrap: 'wrap',
            gap: '15px'
        },
        orderTitle: {
            fontSize: '28px',
            fontWeight: 'bold',
            color: 'white',
            margin: '0'
        },
        statusBadge: {
            padding: '8px 16px',
            background: 'linear-gradient(45deg, #00d4aa, #00b894)',
            borderRadius: '25px',
            fontWeight: 'bold',
            color: 'white',
            fontSize: '14px'
        },
        totalSection: {
            textAlign: 'right'
        },
        totalAmount: {
            fontSize: '32px',
            fontWeight: 'bold',
            color: '#fff',
            marginTop: '5px'
        },
        card: {
            background: 'rgba(255,255,255,0.15)',
            borderRadius: '12px',
            padding: '0',
            marginBottom: '20px',
            boxShadow: '0 8px 25px rgba(0,0,0,0.2)'
        },
        cardHeader: {
            background: 'rgba(255,255,255,0.2)',
            padding: '20px',
            borderRadius: '12px 12px 0 0'
        },
        cardTitle: {
            fontSize: '20px',
            fontWeight: 'bold',
            color: 'white',
            margin: '0'
        },
        itemList: {
            padding: '20px'
        },
        itemRow: {
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            padding: '15px',
            background: 'rgba(255,255,255,0.1)',
            borderRadius: '8px',
            marginBottom: '10px'
        },
        itemName: {
            fontSize: '16px',
            fontWeight: '600',
            color: 'white'
        },
        itemDetails: {
            display: 'flex',
            gap: '15px',
            color: 'rgba(255,255,255,0.8)',
            fontSize: '14px'
        },
        itemTotal: {
            fontSize: '18px',
            fontWeight: 'bold',
            color: '#00d4aa'
        },
        paymentRow: {
            display: 'flex',
            justifyContent: 'space-between',
            padding: '15px',
            background: 'rgba(255,255,255,0.1)',
            borderRadius: '8px',
            marginBottom: '10px'
        },
        loadingBox: {
            minHeight: '400px',
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'white',
            textAlign: 'center'
        },
        errorBox: {
            minHeight: '400px',
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
            color: '#ff6b6b',
            textAlign: 'center',
            background: 'rgba(255,107,107,0.1)',
            padding: '40px',
            borderRadius: '12px',
            border: '1px solid rgba(255,107,107,0.3)'
        }
    };
export default OrderitemManage;
