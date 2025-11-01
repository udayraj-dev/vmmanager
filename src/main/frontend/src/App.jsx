import React, { useState, useEffect } from 'react';
import Header from './components/Header';
import VmTable from './components/VmTable';
import VmCreatePage from './pages/VmCreatePage';

function App() {
  // State for the VM list
  const [vms, setVms] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // State for the form
  const [view, setView] = useState('list'); // 'list' or 'create'

  useEffect(() => {
    // Re-fetch the list of VMs when the form is closed
    fetchVms();
  }, []);

  const fetchVms = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch('/api/vms');
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      const data = await response.json();
      setVms(data.content);
    } catch (e) {
      setError(e.message);
      console.error("Failed to fetch VMs:", e);
    } finally {
      setLoading(false);
    }
  };

  const handleSaveSuccess = () => {
    setView('list');
    fetchVms(); // Re-fetch the list to show the new entry
  };

  return (
    <div className="min-h-screen bg-gray-100 text-gray-800 font-sans">
      <Header />
      <main className="container mx-auto px-4 py-4">
        {view === 'create' ? (
          <VmCreatePage onSaveSuccess={handleSaveSuccess} onCancel={() => setView('list')} />
        ) : (
          <>
            <VmTable vms={vms} loading={loading} error={error} />
            <button onClick={() => setView('create')} className="cursor-pointer rounded-md bg-brand-orange px-4 py-2 text-white hover:bg-brand-orange-lt mr-4 mt-6" type="button">Add VM</button>
          </>
        )}
      </main>
    </div>
  );
}

export default App;