import { useState } from 'react';
import Navbar from './components/Navbar/Navbar';
import SearchForm from './components/SearchForm/SearchForm';
import ResultsTable from './components/ResultsTable/ResultsTable';
import OfferModal from './components/OfferModal/OfferModal';
import axios from 'axios';
import './App.css';

// Construct the API base URL.
// For Vite, environment variables must be prefixed with VITE_
// It defaults to 'http://localhost:8080' if VITE_API_BASE_URL is not set during local 'npm run dev'
// When running in Docker Compose, VITE_API_BASE_URL will be set to 'http://backend:8080'
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

function App() {
  const [offers, setOffers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  // State for modal
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedOfferDetails, setSelectedOfferDetails] = useState(null);

  const handleSearch = async params => {
    setLoading(true);
    setError('');
    setOffers([]); 
    console.log("Data being sent to backend:", params); // For debugging
    try {
      // Use the constructed API_URL for the request
      const apiUrl = `${API_BASE_URL}/api/search`;
      console.log("Requesting API URL:", apiUrl); // For debugging
      const resp = await axios.post(apiUrl, params);
      setOffers(resp.data);
    } catch (err) {
      setError('Search failed – please try again.');
      console.error("Search API error:", err);
      if (err.response) {
        console.error("Error response data:", err.response.data);
        console.error("Error response status:", err.response.status);
      }
    } finally {
      setLoading(false);
    }
  };

  const handleOfferSelect = (offerFromTable) => {
    console.log("Selected flight offer:", offerFromTable); // For debugging
    setSelectedOfferDetails(offerFromTable);
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setSelectedOfferDetails(null); 
  };

  return (
    <div className="App">
      <Navbar />

      <div className="page-container">
        <div className="branding">
          <h1 className="brand-title">AeroKING</h1>
          <p className="brand-tagline">Where every price bows to you</p>
        </div>

        <section className="search-card">
          <SearchForm onSearch={handleSearch} isLoading={loading} />
        </section>

        {loading && <p className="status">Loading…</p>}
        {error && <p className="status error">{error}</p>}

        {!loading && !error && offers.length > 0 && (
          <section className="results">
            <ResultsTable offers={offers} onSelect={handleOfferSelect} />
          </section>
        )}
        
        {!loading && !error && offers.length === 0 && (
           <section className="search-card">
             <p className="no-offers" style={{ color: 'var(--text)', padding: '2rem' }}>
                No flight offers found for your criteria. Try broadening your search!
             </p>
           </section>
        )}
      </div>

      {isModalOpen && selectedOfferDetails && (
        <OfferModal offer={selectedOfferDetails} onClose={closeModal} />
      )}
    </div>
  );
}

export default App;