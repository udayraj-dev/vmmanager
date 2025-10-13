<script>
  import { push } from 'svelte-spa-router';

  let vm = {
    ipAddress: '',
    portNumber: 22,
    username: 'root',
    password: '',
    osName: '',
    hostName: '',
    cpuCores: '',
    rootPartitionSize: '',
    totalRamSize: '',
    macAddress: '',
    category: '',
    purpose: '',
    ownerName: '',
    isLinux: true,
  };

  let loading = false;
  let error = null;

  $: {
    if (vm.isLinux) {
      vm.portNumber = 22;
      vm.username = 'root';
    } else {
      vm.portNumber = 3389;
      vm.username = 'Administrator';
    }
  }

  async function saveVM() {
    loading = true;
    error = null;
    try {
      const res = await fetch('/vms', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(vm)
      });

      if (!res.ok) {
        const err = await res.json();
        throw new Error(err.message || 'Failed to save VM');
      }

      const savedVm = await res.json();
      alert('VM saved successfully!');
      push(`/details/${savedVm.id}`);
    } catch (e) {
      error = e.message;
      console.error(e);
    } finally {
      loading = false;
    }
  }
</script>

<style>
  form {
    display: grid;
    grid-template-columns: 150px 1fr;
    gap: 1rem;
    max-width: 600px;
  }
  label {
    font-weight: bold;
    text-align: right;
  }
  .full-width {
    grid-column: 1 / -1;
  }
  hr.full-width {
    margin-top: 1rem;
    margin-bottom: 1rem;
  }
  button {
    grid-column: 2 / 3;
    width: fit-content;
  }
</style>

<h2>Add VM</h2>

{#if error}
  <p style="color: red;" class="full-width">Error: {error}</p>
{/if}

<form on:submit|preventDefault={saveVM}>
  <label for="category">Category</label>
  <input id="category" bind:value={vm.category} placeholder="e.g., Web Servers" required />

  <label for="purpose">Purpose</label>
  <textarea id="purpose" bind:value={vm.purpose} placeholder="e.g., Hosting the main company website"></textarea>

  <label for="ownerName">Owner Name</label>
  <input id="ownerName" bind:value={vm.ownerName} placeholder="e.g., John Doe" required />

  <label for="ipAddress">IP Address</label>
  <input id="ipAddress" bind:value={vm.ipAddress} placeholder="e.g., 192.168.1.10" required />

  <label for="isLinux">Is Linux?</label>
  <input id="isLinux" type="checkbox" bind:checked={vm.isLinux} />

  <label for="username">Username</label>
  <input id="username" bind:value={vm.username} required />

  <label for="password">Password</label>
  <input id="password" type="password" bind:value={vm.password} required />

  <label for="portNumber">Port</label>
  <input id="portNumber" type="number" bind:value={vm.portNumber} required readonly={!vm.isLinux} />

  <hr class="full-width" />

  <div class="full-width">
    <em>The following fields are auto-discovered for Linux VMs. For Windows, please fill them in manually.</em>
  </div>

  <label for="hostName">Hostname</label>
  <input id="hostName" bind:value={vm.hostName} placeholder="e.g., web-server-01" required readonly={vm.isLinux} />

  <label for="osName">OS Name</label>
  <input id="osName" bind:value={vm.osName} placeholder="e.g., Ubuntu 22.04" required readonly={vm.isLinux} />

  <label for="cpuCores">CPU Cores</label>
  <input id="cpuCores" bind:value={vm.cpuCores} placeholder="e.g., 4" required readonly={vm.isLinux} />

  <label for="totalRamSize">RAM Size</label>
  <input id="totalRamSize" bind:value={vm.totalRamSize} placeholder="e.g., 8GB" required readonly={vm.isLinux} />

  <label for="rootPartitionSize">Root Partition Size</label>
  <input id="rootPartitionSize" bind:value={vm.rootPartitionSize} placeholder="e.g., 100GB" required readonly={vm.isLinux} />

  <label for="macAddress">MAC Address</label>
  <input id="macAddress" bind:value={vm.macAddress} placeholder="e.g., 00:1A:2B:3C:4D:5E" required readonly={vm.isLinux} />

  <button type="submit" disabled={loading}>
    {#if loading}Saving...{:else}Save{/if}
  </button>
</form>