<script>
    // @ts-nocheck
    import { onMount } from 'svelte';
    import { link } from 'svelte-spa-router';

    let vms = {};
    let error = null;
    let loading = true;

    onMount(async () => {
        try {
            const res = await fetch('/api/vms');
            if (!res.ok) {
                const err = await res.json();
                throw new Error(err.message || 'Failed to fetch VMs');
            }
            vms = await res.json();
        } catch (e) {
            error = e.message;
            console.error(e);
        } finally {
            loading = false;
        }
    });
</script>

<div class="container mx-auto p-8">
    <h2 class="text-2xl font-bold mb-6">Virtual Machines</h2>

    {#if loading}
        <p>Loading...</p>
    {:else if error}
        <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
            <strong class="font-bold">Error:</strong>
            <span class="block sm:inline">{error}</span>
        </div>
    {:else if vms.content.length === 0}
        <p class="shadow overflow-hidden border-b border-gray-200 sm:rounded-lg">Add some VMs to see them here.</p>
    {:else}
        <div class="shadow overflow-hidden border-b border-gray-200 sm:rounded-lg">
            <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                    <tr>
                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">IP Address</th>
                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">OS</th>
                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Owner</th>
                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                        <th scope="col" class="relative px-6 py-3">
                            <span class="sr-only">Details</span>
                        </th>
                    </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                    {#each vms as vm}
                        <tr>
                            <td class="px-6 py-4 whitespace-nowrap">
                                <div class="text-sm font-medium text-gray-900">{vm.hostName}</div>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">
                                <div class="text-sm text-gray-900">{vm.ipAddress}</div>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">
                                <div class="text-sm text-gray-900">{vm.osName}</div>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{vm.ownerName}</td>
                            <td class="px-6 py-4 whitespace-nowrap">
                                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800"> {vm.status} </span>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                                <a href="#/details/{vm.id}" use:link class="text-indigo-600 hover:text-indigo-900">Details</a>
                            </td>
                        </tr>
                    {/each}
                </tbody>
            </table>
        </div>
    {/if}
</div>