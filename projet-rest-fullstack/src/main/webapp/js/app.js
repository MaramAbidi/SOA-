// Configuration de l'API
const API_BASE_URL = 'http://localhost:8080/ProjetREST/api/persons';

// Charger toutes les personnes au démarrage
document.addEventListener('DOMContentLoaded', () => {
    loadAllPersons();
    setupFormSubmit();
});

// Configuration du formulaire
function setupFormSubmit() {
    const form = document.getElementById('personForm');
    form.addEventListener('submit', (e) => {
        e.preventDefault();
        const personId = document.getElementById('personId').value;
        
        if (personId) {
            updatePerson(personId);
        } else {
            addPerson();
        }
    });
}

// Charger toutes les personnes
async function loadAllPersons() {
    try {
        showLoading();
        const response = await fetch(API_BASE_URL);
        
        if (!response.ok) {
            throw new Error('Erreur lors du chargement des personnes');
        }
        
        const persons = await response.json();
        displayPersons(persons);
        
    } catch (error) {
        console.error('Erreur:', error);
        showNotification('Erreur lors du chargement des personnes', 'error');
        document.getElementById('personsList').innerHTML = 
            '<div class="empty-state"><p>❌ Erreur de chargement</p></div>';
    }
}

// Rechercher une personne par nom
async function searchPerson() {
    const searchInput = document.getElementById('searchInput').value.trim();
    
    if (!searchInput) {
        showNotification('Veuillez entrer un nom à rechercher', 'error');
        return;
    }
    
    try {
        showLoading();
        const response = await fetch(`${API_BASE_URL}/search?nom=${encodeURIComponent(searchInput)}`);
        
        if (!response.ok) {
            throw new Error('Erreur lors de la recherche');
        }
        
        const persons = await response.json();
        displayPersons(persons);
        
        if (persons.length === 0) {
            showNotification('Aucune personne trouvée', 'error');
        }
        
    } catch (error) {
        console.error('Erreur:', error);
        showNotification('Erreur lors de la recherche', 'error');
    }
}

// Ajouter une personne
async function addPerson() {
    const person = getFormData();
    
    try {
        const response = await fetch(API_BASE_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(person)
        });
        
        if (!response.ok) {
            throw new Error('Erreur lors de l\'ajout');
        }
        
        showNotification('✅ Personne ajoutée avec succès', 'success');
        resetForm();
        loadAllPersons();
        
    } catch (error) {
        console.error('Erreur:', error);
        showNotification('❌ Erreur lors de l\'ajout', 'error');
    }
}

// Modifier une personne
async function updatePerson(id) {
    const person = getFormData();
    
    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(person)
        });
        
        if (!response.ok) {
            throw new Error('Erreur lors de la modification');
        }
        
        showNotification('✅ Personne modifiée avec succès', 'success');
        resetForm();
        loadAllPersons();
        
    } catch (error) {
        console.error('Erreur:', error);
        showNotification('❌ Erreur lors de la modification', 'error');
    }
}

// Supprimer une personne
async function deletePerson(id, nom) {
    if (!confirm(`Voulez-vous vraiment supprimer ${nom} ?`)) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) {
            throw new Error('Erreur lors de la suppression');
        }
        
        showNotification('✅ Personne supprimée avec succès', 'success');
        loadAllPersons();
        
    } catch (error) {
        console.error('Erreur:', error);
        showNotification('❌ Erreur lors de la suppression', 'error');
    }
}

// Préparer le formulaire pour la modification
function editPerson(person) {
    document.getElementById('personId').value = person.id;
    document.getElementById('nom').value = person.nom;
    document.getElementById('prenom').value = person.prenom;
    document.getElementById('email').value = person.email;
    document.getElementById('telephone').value = person.telephone || '';
    document.getElementById('adresse').value = person.adresse || '';
    
    document.getElementById('formTitle').textContent = '✏️ Modifier une personne';
    
    // Scroll vers le formulaire
    document.querySelector('.form-section').scrollIntoView({ behavior: 'smooth' });
}

// Afficher les personnes
function displayPersons(persons) {
    const container = document.getElementById('personsList');
    
    if (!persons || persons.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <p>📭 Aucune personne trouvée</p>
            </div>
        `;
        return;
    }
    
    container.innerHTML = persons.map(person => `
        <div class="person-card">
            <h3>${person.prenom} ${person.nom}</h3>
            <div class="person-info">
                <p><strong>📧 Email:</strong> ${person.email}</p>
                ${person.telephone ? `<p><strong>📞 Téléphone:</strong> ${person.telephone}</p>` : ''}
                ${person.adresse ? `<p><strong>📍 Adresse:</strong> ${person.adresse}</p>` : ''}
            </div>
            <div class="person-actions">
                <button class="btn-edit" onclick='editPerson(${JSON.stringify(person)})'>✏️ Modifier</button>
                <button class="btn-danger" onclick="deletePerson(${person.id}, '${person.prenom} ${person.nom}')">🗑️ Supprimer</button>
            </div>
        </div>
    `).join('');
}

// Récupérer les données du formulaire
function getFormData() {
    return {
        nom: document.getElementById('nom').value.trim(),
        prenom: document.getElementById('prenom').value.trim(),
        email: document.getElementById('email').value.trim(),
        telephone: document.getElementById('telephone').value.trim(),
        adresse: document.getElementById('adresse').value.trim()
    };
}

// Réinitialiser le formulaire
function resetForm() {
    document.getElementById('personForm').reset();
    document.getElementById('personId').value = '';
    document.getElementById('formTitle').textContent = '➕ Ajouter une personne';
}

// Afficher un message de chargement
function showLoading() {
    document.getElementById('personsList').innerHTML = `
        <div class="loading">⏳ Chargement...</div>
    `;
}

// Afficher une notification
function showNotification(message, type) {
    const notification = document.getElementById('notification');
    notification.textContent = message;
    notification.className = `notification ${type}`;
    
    // Afficher
    setTimeout(() => {
        notification.classList.add('show');
    }, 100);
    
    // Masquer après 3 secondes
    setTimeout(() => {
        notification.classList.remove('show');
    }, 3000);
}