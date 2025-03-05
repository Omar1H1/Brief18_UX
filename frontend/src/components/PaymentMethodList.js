import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { getPaymentMethods, createPaymentMethod, deletePaymentMethod } from '../services/paymentMethodService';
import Modal from './Modal';

export default function PaymentMethodList() {
  const { auth } = useAuth();
  const [paymentMethods, setPaymentMethods] = useState([]);
  const [showNewForm, setShowNewForm] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    lastDigits: ''
  });

  useEffect(() => {
    getPaymentMethods(auth).then(setPaymentMethods);
  }, [auth]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await createPaymentMethod(auth, formData);
      const updatedMethods = await getPaymentMethods(auth);
      setPaymentMethods(updatedMethods);
      setShowNewForm(false);
      setFormData({ name: '', lastDigits: '' });
    } catch (error) {
      console.error('Failed to create payment method:', error);
    }
  };

  const handleDelete = async (methodId) => {
    if (window.confirm('Are you sure you want to delete this payment method?')) {
      try {
        await deletePaymentMethod(auth, methodId);
        setPaymentMethods(paymentMethods.filter(method => method.id !== methodId));
      } catch (error) {
        console.error('Failed to delete payment method:', error);
      }
    }
  };

  return (
    <div className="payment-method-list">
      <h2>Payment Methods</h2>
      <button className="fab-button" onClick={() => setShowNewForm(true)}>+</button>
      
      <div className="payment-methods">
        {paymentMethods.map(method => (
          <div key={method.id} className="payment-method-item">
            <div className="method-content">
              <div className="method-name">{method.name}</div>
              <div className="method-digits">**** **** **** {method.lastDigits}</div>
            </div>
            <button 
              className="delete-button"
              onClick={() => handleDelete(method.id)}
              aria-label={`Delete ${method.name} payment method`}
            >
              🗑️
            </button>
          </div>
        ))}
      </div>

      <Modal
        isOpen={showNewForm}
        onClose={() => setShowNewForm(false)}
        title="New Payment Method"
      >
        <form onSubmit={handleSubmit} className="payment-method-form">
          <input
            type="text"
            placeholder="Method Name"
            value={formData.name}
            onChange={e => setFormData({...formData, name: e.target.value})}
            required
          />
          <input
            type="text"
            placeholder="Last 4 Digits"
            value={formData.lastDigits}
            pattern="\d{4}"
            maxLength="4"
            onChange={e => setFormData({...formData, lastDigits: e.target.value})}
            required
          />
          <button type="submit">Create Payment Method</button>
        </form>
      </Modal>
    </div>
  );
}
