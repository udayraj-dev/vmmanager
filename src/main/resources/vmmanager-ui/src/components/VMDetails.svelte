<script>
  import { params } from 'svelte-spa-router';
  let vm = null;
  let error = null;
  let loading = false;

  $: if ($params.id) {
    loading = true;
    error = null;
    fetch(`/vms/${$params.id}`)
      .then(async res => {
        if (!res.ok) {
          const err = await res.json();
          throw new Error(err.message || 'Failed to fetch VM details');
        }
        return res.json();
      })
      .then(data => {
        vm = data;
      })
      .catch(e => {
        error = e.message;
        console.error(e);
      })
      .finally(() => {
        loading = false;
      });
  }
</script>

<style>
  .details-grid {
    display: grid;
    grid-template-columns: 150px 1fr;
    gap: 0.5rem;
  }
  .details-grid > div:nth-child(odd) {
    font-weight: bold;
  }
</style>

<h2>VM Details</h2>

{#if loading}
  <p>Loading...</p>
{:else if error}
  <p style="color: red;">Error: {error}</p>
{:else if vm}
  <div class="details-grid">
    <div>ID:</div>
    <div>{vm.id}</div>

    <div>Hostname:</div>
    <div>{vm.hostName}</div>

    <div>IP Address:</div>
    <div>{vm.ipAddress}:{vm.portNumber}</div>

    <div>OS:</div>
    <div>{vm.osName}</div>

    <div>CPU Cores:</div>
    <div>{vm.cpuCores}</div>

    <div>RAM:</div>
    <div>{vm.totalRamSize}</div>

    <div>Root Partition:</div>
    <div>{vm.rootPartitionSize}</div>

    <div>MAC Address:</div>
    <div>{vm.macAddress}</div>

    <div>Status:</div>
    <div>{vm.status}</div>

    <div>Owner:</div>
    <div>{vm.ownerName}</div>

    <div>Category:</div>
    <div>{vm.category}</div>

    <div>Purpose:</div>
    <div>{vm.purpose}</div>

    <div>Created:</div>
    <div>{new Date(vm.createdDate).toLocaleString()}</div>

    <div>Last Updated:</div>
    <div>{new Date(vm.lastUpdatedDate).toLocaleString()}</div>
  </div>
{:else}
  <p>No VM details found for this ID.</p>
{/if}
