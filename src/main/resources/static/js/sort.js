// static/js/sort.js

document.addEventListener('alpine:init', () => {
    Alpine.data('productFilterSort', () => ({
        items: [],
        filteredItems: [],
        selectedCategory: 'all',
        selectedAvailability: 'all',
        sortBy: 'newest',
        categories: [],

        init() {
            console.log('Alpine.js x-data initialisiert.');
            this.fetchProducts();
        },

        fetchProducts() {
            console.log('Starte fetchProducts()...');
            fetch('/api/v1/public/products')
                .then(response => {
                    console.log('Fetch-Antwort erhalten. Status:', response.status, 'OK:', response.ok);
                    if (!response.ok) {
                        throw new Error('Fehler beim Laden der Produkte: ' + response.statusText);
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('Produktdaten erfolgreich abgerufen:', data);
                    this.items = data;
                    this.categories = [...new Set(data.map(item => item.category))];
                    console.log('Gefundene Kategorien:', this.categories);
                    this.applyFiltersAndSort();
                    console.log('applyFiltersAndSort() wurde aufgerufen.');
                })
                .catch(error => {
                    console.error('FEHLER beim Abrufen der Produkte:', error);
                    this.items = [];
                    this.filteredItems = [];
                    this.categories = [];
                });
        },

        applyFiltersAndSort() {
            console.log('applyFiltersAndSort() wird ausgeführt...');
            console.log('Aktuelle Filter: Kategorie=', this.selectedCategory, ', Verfügbarkeit=', this.selectedAvailability, ', Sortierung=', this.sortBy);

            let tempItems = [...this.items];
            console.log('Anzahl Items vor Filterung:', tempItems.length);

            if (this.selectedCategory !== 'all') {
                tempItems = tempItems.filter(item => item.category === this.selectedCategory);
                console.log('Anzahl Items nach Kategorie-Filter (' + this.selectedCategory + '):', tempItems.length);
            }

            if (this.selectedAvailability !== 'all') {
                tempItems = tempItems.filter(item => {
                    let match = false;
                    if (this.selectedAvailability === 'online') {
                        match = item.onlineStatus === true;
                    } else if (this.selectedAvailability === 'offline') {
                        match = item.onlineStatus === false;
                    }
                    console.log('Item:', item.name, 'Online Status:', item.onlineStatus, 'Match Verfügbarkeit:', match);
                    return match;
                });
                console.log('Anzahl Items nach Verfügbarkeits-Filter (' + this.selectedAvailability + '):', tempItems.length);
            }

            try {
                tempItems.sort((a, b) => {
                    console.log('Sortiere:', a.name, 'vs', b.name);
                    if (this.sortBy === 'newest' || this.sortBy === 'oldest') {
                        const dateA = Array.isArray(a.createdAt)
                            ? new Date(a.createdAt[0], a.createdAt[1] - 1, a.createdAt[2], a.createdAt[3], a.createdAt[4], a.createdAt[5], a.createdAt[6] / 1000000)
                            : new Date(a.createdAt);
                        const dateB = Array.isArray(b.createdAt)
                            ? new Date(b.createdAt[0], b.createdAt[1] - 1, b.createdAt[2], b.createdAt[3], b.createdAt[4], b.createdAt[5], b.createdAt[6] / 1000000)
                            : new Date(b.createdAt);

                        if (isNaN(dateA.getTime()) || isNaN(dateB.getTime())) {
                            console.warn('Datumsparsen fehlgeschlagen für:', a.createdAt, 'oder', b.createdAt);
                            return 0;
                        }

                        if (this.sortBy === 'newest') {
                            return dateB.getTime() - dateA.getTime();
                        } else {
                            return dateA.getTime() - dateB.getTime();
                        }
                    } else if (this.sortBy === 'alpha-asc') {
                        return a.name.localeCompare(b.name);
                    } else if (this.sortBy === 'alpha-desc') {
                        return b.name.localeCompare(a.name);
                    }
                    return 0;
                });
                console.log('Sortierung abgeschlossen.');
            } catch (sortError) {
                console.error('FEHLER bei der Sortierung:', sortError);
            }

            this.filteredItems = tempItems;
            console.log('filteredItems aktualisiert. Anzahl der anzuzeigenden Produkte:', this.filteredItems.length);
            if (this.filteredItems.length > 0) {
                console.log('Erstes gefiltertes Produkt:', this.filteredItems[0]);
            }
        }
    }));
});