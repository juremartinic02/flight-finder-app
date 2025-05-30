import React from 'react';
import './OfferModal.css'; // Stlyes for OfferModal component

/**
 * Function that formats an ISO dateTime string into a more readable "dd/MM/yyyy HH:mm" format for display to user.
 * Includes basic error handling for invalid date strings.
 */
const formatDisplayDateTime = dateTimeStr => {
  // If the input string is null or empty, return - as a placeholder
  if (!dateTimeStr) return '—';
  try {
    // Trying to parse the string into a JS date object
    const dateObj = new Date(dateTimeStr);
    if (isNaN(dateObj.getTime())) {
      // Log an error if the date string is invalid and return the original string
      console.error("Invalid date string passed to formatDisplayDateTime in Modal:", dateTimeStr);
      return dateTimeStr;
    }
    // Extrapolate date and time components
    const day = String(dateObj.getDate()).padStart(2, '0');
    const month = String(dateObj.getMonth() + 1).padStart(2, '0');
    const year = dateObj.getFullYear();
    const hours = String(dateObj.getHours()).padStart(2, '0');
    const minutes = String(dateObj.getMinutes()).padStart(2, '0');
    return `${day}/${month}/${year} ${hours}:${minutes}`;
  } catch (error) {
    console.error("Error formatting date-time in modal:", dateTimeStr, error);
    return dateTimeStr;
  }
};

// React functional component that displays flight offer details in a modal dialog
export default function OfferModal({ offer, onClose }) {
  if (!offer) {
    return null;
  }

  const totalPassengers = (offer.adults || 0) + (offer.children || 0) + (offer.infants || 0);

  const handleBuyNow = () => {
    console.log("Buy Now clicked for offer ID:", offer.id, offer);
    alert(`Simulating "Buy Now" for flight:\nOrigin: ${offer.origin} to Destination: ${offer.destination}\nPrice: ${offer.totalPrice} ${offer.currency}\n\n(This is a placeholder for actual booking functionality that will be implemented in future development.)`);
    onClose();
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={e => e.stopPropagation()}>
        <h2>Flight Offer Details</h2>
        <button className="modal-close-button" onClick={onClose}>×</button>
        
        <div className="offer-details-grid">
          <div className="detail-item"><strong>ID:</strong> {offer.id}</div>
          <div className="detail-item"><strong>Origin:</strong> {offer.origin}</div>
          <div className="detail-item"><strong>Destination:</strong> {offer.destination}</div>
          <div className="detail-item"><strong>Departure:</strong> {formatDisplayDateTime(offer.departureDateTime)}</div>
          <div className="detail-item"><strong>Return:</strong> {formatDisplayDateTime(offer.returnDateTime)}</div>
          <div className="detail-item"><strong>Outbound Stops:</strong> {offer.outboundTransfers === 0 ? 'Direct' : (typeof offer.outboundTransfers === 'number' ? offer.outboundTransfers : 'N/A')}</div>
          <div className="detail-item"><strong>Inbound Stops:</strong> {offer.returnDateTime && (offer.inboundTransfers === 0 ? 'Direct' : (typeof offer.inboundTransfers === 'number' ? offer.inboundTransfers : 'N/A'))}</div>
          <div className="detail-item"><strong>Passengers:</strong> {totalPassengers}</div>
          <div className="detail-item"><strong>Total Price:</strong> {offer.totalPrice} {offer.currency}</div>
        </div>

        <div className="modal-actions">
          <button className="buy-now-button" onClick={handleBuyNow}>
            Buy Now
          </button>
        </div>
      </div>
    </div>
  );
}