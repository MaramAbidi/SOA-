package com.rest.service;

import com.rest.model.Person;
import com.rest.util.EMUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Service REST pour la gestion des personnes
 * Endpoints: /api/persons
 */
@Path("/persons")
public class PersonService {

    /**
     * Récupérer toutes les personnes
     * GET /api/persons
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPersons() {
        EntityManager em = EMUtil.getEntityManager();
        try {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
            List<Person> persons = query.getResultList();
            return Response.ok(persons).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        } finally {
            em.close();
        }
    }

    /**
     * Récupérer une personne par ID
     * GET /api/persons/{id}
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonById(@PathParam("id") Long id) {
        EntityManager em = EMUtil.getEntityManager();
        try {
            Person person = em.find(Person.class, id);
            if (person != null) {
                return Response.ok(person).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Personne non trouvée avec l'ID: " + id + "\"}").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        } finally {
            em.close();
        }
    }

    /**
     * Rechercher une personne par nom
     * GET /api/persons/search?nom=Dupont
     */
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchPersonByName(@QueryParam("nom") String nom) {
        EntityManager em = EMUtil.getEntityManager();
        try {
            TypedQuery<Person> query = em.createQuery(
                    "SELECT p FROM Person p WHERE LOWER(p.nom) LIKE LOWER(:nom)", 
                    Person.class);
            query.setParameter("nom", "%" + nom + "%");
            List<Person> persons = query.getResultList();
            return Response.ok(persons).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        } finally {
            em.close();
        }
    }

    /**
     * Ajouter une nouvelle personne
     * POST /api/persons
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPerson(Person person) {
        EntityManager em = EMUtil.getEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            em.persist(person);
            tx.commit();
            return Response.status(Response.Status.CREATED).entity(person).build();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        } finally {
            em.close();
        }
    }

    /**
     * Modifier une personne existante
     * PUT /api/persons/{id}
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePerson(@PathParam("id") Long id, Person updatedPerson) {
        EntityManager em = EMUtil.getEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            
            Person person = em.find(Person.class, id);
            if (person != null) {
                person.setNom(updatedPerson.getNom());
                person.setPrenom(updatedPerson.getPrenom());
                person.setEmail(updatedPerson.getEmail());
                person.setTelephone(updatedPerson.getTelephone());
                person.setAdresse(updatedPerson.getAdresse());
                
                em.merge(person);
                tx.commit();
                return Response.ok(person).build();
            } else {
                tx.rollback();
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Personne non trouvée avec l'ID: " + id + "\"}").build();
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        } finally {
            em.close();
        }
    }

    /**
     * Supprimer une personne
     * DELETE /api/persons/{id}
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePerson(@PathParam("id") Long id) {
        EntityManager em = EMUtil.getEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            
            Person person = em.find(Person.class, id);
            if (person != null) {
                em.remove(person);
                tx.commit();
                return Response.ok()
                        .entity("{\"message\": \"Personne supprimée avec succès\"}").build();
            } else {
                tx.rollback();
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Personne non trouvée avec l'ID: " + id + "\"}").build();
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        } finally {
            em.close();
        }
    }
}