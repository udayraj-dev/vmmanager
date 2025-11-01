import React, { useState } from 'react';
import VmForm from '../components/VmForm';

const INITIAL_VM_STATE = {
    category: '',
    purpose: '',
    ownerName: '',
    ipAddress: '',
    isLinux: true,
    username: '',
    password: '',
    portNumber: 22,
    hostName: '',
    osName: '',
    cpuCores: '',
    totalRamSize: '',
    rootPartitionSize: '',
    macAddress: '',
};

function VmCreatePage({ onSaveSuccess, onCancel }) {
    const [currentVm, setCurrentVm] = useState(INITIAL_VM_STATE);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const saveVM = async () => {
        setLoading(true);
        setError(null);

        try {
            const response = await fetch('/api/vms', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(currentVm),
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({ message: `Failed to save with status: ${response.status}` }));
                throw new Error(errorData.message || 'An unknown error occurred while saving.');
            }

            // On success, call the callback to notify the parent component.
            onSaveSuccess();

        } catch (e) {
            setError(e.message);
            console.error("Failed to save VM:", e);
        } finally {
            setLoading(false);
        }
    };

    const testConnection = async () => {
        setLoading(true);
        setError(null);

        const { ipAddress, portNumber, username, password } = currentVm;
        const sshLogin = { ipAddress, portNumber, username, password };

        try {
            const response = await fetch('/api/vms/test-connection', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(sshLogin),
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({ message: `Connection failed with status: ${response.status}` }));
                throw new Error(errorData.message || 'An unknown error occurred during connection test.');
            }

            const discoveredInfo = await response.json();

            setCurrentVm(prevVm => ({
                ...prevVm,
                hostName: discoveredInfo.hostName,
                osName: discoveredInfo.osName,
                cpuCores: discoveredInfo.cpuCores,
                totalRamSize: discoveredInfo.totalRamSize,
                rootPartitionSize: discoveredInfo.rootPartitionSize,
                macAddress: discoveredInfo.macAddress,
            }));

            if (discoveredInfo.portNumber) { setCurrentVm(prevVm => ({ ...prevVm, portNumber: discoveredInfo.portNumber })); }
        } catch (e) {
            setError(e.message);
            console.error("Failed to test connection:", e);
        } finally {
            setLoading(false);
        }
    };

    return (
        <VmForm
            vm={currentVm}
            setVm={setCurrentVm}
            saveVM={saveVM}
            testConnection={testConnection}
            onCancel={onCancel}
            loading={loading}
            error={error}
        />
    );
}

export default VmCreatePage;