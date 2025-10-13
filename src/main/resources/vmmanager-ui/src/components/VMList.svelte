<script>
    import { onMount } from 'svelte';
    let vms = [];
  
    onMount(async () => {
      const res = await fetch('/vms');
      vms = await res.json();
    });
  </script>
  
  <h2>Virtual Machines</h2>
  <ul>
    {#each vms as vm}
      <li>
        <strong>{vm.name}</strong> - {vm.status}
        <a href={`/details/${vm.id}`}>Details</a>
      </li>
    {/each}
  </ul>