import React from 'react';
// ResultTable syling is in App.css

/**
 * Function that displays the result table to user with viable offers for parameters that he gave to the app.
 * Includes basic error handling.
 */
export default function ResultsTable({ offers, onSelect }) {
  // If there are no offers, return a "No offers to display" message
  if (!offers || !offers.length) {
    return <p className="no-offers">No offers to display.</p>;
  }

  /**
 * Helper function that formats an ISO dateTime string into a more readable "dd/MM/yyyy HH:mm" format for display to user.
 * Includes basic error handling for invalid date strings.
 */
  const formatDisplayDateTime = dateTimeStr => {
    // If the input string is null or empty, return - as a placeholder
    if (!dateTimeStr) return '—';
    try {
      const dateObj = new Date(dateTimeStr);
      const day = String(dateObj.getDate()).padStart(2, '0');
      const month = String(dateObj.getMonth() + 1).padStart(2, '0');
      const year = dateObj.getFullYear();
      const hours = String(dateObj.getHours()).padStart(2, '0');
      const minutes = String(dateObj.getMinutes()).padStart(2, '0');
      return `${day}/${month}/${year} ${hours}:${minutes}`;
    } catch (error) {
      console.error("Error formatting date-time:", dateTimeStr, error);
      return dateTimeStr;
    }
  };

  // Render the table structure
  return (
    <div className="results-table-wrapper">
      <table className="results-table">
        <thead>
          <tr>
            <th>Dep. Airport</th>
            <th>Arr. Airport</th>
            <th>Departure</th>
            <th>Return</th>
            <th>Stops (Out/In)</th>
            <th>Passengers</th>
            <th>Total Price</th>
            <th>Currency</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {offers.map(o => {
            const totalPassengers = (o.adults || 0) + (o.children || 0) + (o.infants || 0);
  
            // Return the JSX for a single table row (<tr>) representing one flight offer
            return (
              <tr key={o.id}>
                <td>{o.departureAirportCode || o.origin}</td>
                <td>{o.returnAirportCode || o.destination}</td>
                <td>{formatDisplayDateTime(o.departureDateTime)}</td>
                <td>{formatDisplayDateTime(o.returnDateTime)}</td>
                <td>
                  {o.outboundTransfers === 0 ? 'Direct' : (typeof o.outboundTransfers === 'number' ? o.outboundTransfers : 'N/A')}
                  {o.returnDateTime && (o.inboundTransfers === 0 ? ' / Direct' : (typeof o.inboundTransfers === 'number' ? ` / ${o.inboundTransfers}` : ' / N/A'))}
                </td>
                <td>{totalPassengers > 0 ? totalPassengers : 'N/A'}</td>
                <td>{o.totalPrice}</td>
                <td>{o.currency}</td>
                <td>
                  <button
                    type="button"
                    className="search-button"
                    onClick={() => onSelect?.(o)}
                  >
                    Select
                  </button>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}