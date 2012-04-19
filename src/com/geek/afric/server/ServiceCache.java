/**
 * 
 */
package com.geek.afric.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.geek.afric.shared.AbstractFichier;
import com.geek.afric.shared.Comment;
import com.geek.afric.shared.Dossier;
import com.geek.afric.shared.Fichier;
import com.google.appengine.api.memcache.MemcacheService;

/**
 * @author Mcicheick
 * 
 */
public class ServiceCache {
	private static String file_prefix = "FILE/";
	private static String files_prefix = "FILES/";
	private static String dossier_prefix = "DOSSIER";
	private static String dossiers_prefix = "DOSSIERS";
	private static final String comments_prefix = "COMMENTS/";
	//private static final String comment_prefix = "COMMENT/";
	private final MemcacheService memcache;
	
	Set<String> keys = new HashSet<String>();

	public ServiceCache(MemcacheService memcache) {
		this.memcache = memcache;
	}

	public void deleteFile(String fileId) {
		keys.remove(file_prefix + fileId);
		memcache.delete(file_prefix + fileId);
	}

	public void deleteFiles(String userId) {
		keys.remove(files_prefix + userId);
		memcache.delete(files_prefix + userId);
	}

	public Fichier putFile(Fichier file) {
		keys.add(file_prefix + file.getId());
		memcache.put(file_prefix + file.getId(), file);
		return file;
	}

	public ArrayList<Fichier> putFiles(String userId, ArrayList<Fichier> files) {
		keys.add(files_prefix + userId);
		memcache.put(files_prefix + userId, files);
		return files;
	}

	public void clear() {
		keys.clear();
		memcache.clearAll();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Fichier> getFiles(String userId) {
		return (ArrayList<Fichier>) memcache.get(files_prefix + userId);
	}

	public Fichier getFile(String fileId) {
		return (Fichier) memcache.get(file_prefix + fileId);
	}

	public void deleteAll(ArrayList<String> fileKeys) {
		for (int i = 0; i < fileKeys.size(); i++) {
			deleteFiles(fileKeys.get(i));
		}
	}

	public void clear(String id) {
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			if(key.contains(id)) {
				memcache.delete(key);
			}
		}
	}

	public Dossier getDossier(String dossierId) {
		return (Dossier) memcache.get(dossier_prefix + dossierId);
	}

	public Dossier putDossier(Dossier dossier) {
		memcache.put(dossier_prefix + dossier.getId(), dossier);
		return dossier;
	}

	public void deleteDossier(String dossierId) {
		keys.remove(dossier_prefix + dossierId);
		memcache.delete(dossier_prefix + dossierId);
	}

	public ArrayList<Dossier> putDossiers(String userId, ArrayList<Dossier> dossiers) {
		keys.add(dossiers_prefix + userId);
		memcache.put(dossiers_prefix + userId, dossiers);
		return dossiers;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Dossier> getDossiers(String userId) {
		return (ArrayList<Dossier>) memcache.get(dossiers_prefix + userId);
	}

	public void deleteDossiers(String userId) {
		keys.remove(dossiers_prefix + userId);
		memcache.delete(dossiers_prefix + userId);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<AbstractFichier> getRacines(String string) {
		return ((ArrayList<AbstractFichier>) memcache.get(string));
	}

	public ArrayList<AbstractFichier> putRacines(String string, ArrayList<AbstractFichier> all) {
		keys.add(string);
		memcache.put(string, all);
		return all;
	}
    
    public void deleteComments(String fileId){
    	memcache.delete(comments_prefix + fileId);
    }
    
    @SuppressWarnings("unchecked")
	public ArrayList<Comment> getObjectComments(String fileId) {
    	return (ArrayList<Comment>) memcache.get(comments_prefix + fileId);
    }
    
    public ArrayList<Comment> putObjectComments(String fileId,  ArrayList<Comment> comments) {
    	memcache.put(fileId, comments);
    	return comments;
    }
}
