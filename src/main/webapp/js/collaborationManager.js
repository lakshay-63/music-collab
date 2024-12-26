class CollaborationManager {
    constructor() {
      this.collaborators = new Map();
      this.projects = new Map();
    }
  
    addCollaborator(userId, name, role) {
      this.collaborators.set(userId, { name, role, projects: new Set() });
    }
  
    removeCollaborator(userId) {
      const collaborator = this.collaborators.get(userId);
      if (collaborator) {
        collaborator.projects.forEach(projectId => {
          this.removeCollaboratorFromProject(userId, projectId);
        });
        this.collaborators.delete(userId);
      }
    }
  
    createProject(projectId, name, ownerId) {
      this.projects.set(projectId, { name, ownerId, collaborators: new Set([ownerId]) });
      const owner = this.collaborators.get(ownerId);
      if (owner) {
        owner.projects.add(projectId);
      }
    }
  
    deleteProject(projectId) {
      const project = this.projects.get(projectId);
      if (project) {
        project.collaborators.forEach(userId => {
          const collaborator = this.collaborators.get(userId);
          if (collaborator) {
            collaborator.projects.delete(projectId);
          }
        });
        this.projects.delete(projectId);
      }
    }
  
    addCollaboratorToProject(userId, projectId) {
      const project = this.projects.get(projectId);
      const collaborator = this.collaborators.get(userId);
      if (project && collaborator) {
        project.collaborators.add(userId);
        collaborator.projects.add(projectId);
      }
    }
  
    removeCollaboratorFromProject(userId, projectId) {
      const project = this.projects.get(projectId);
      const collaborator = this.collaborators.get(userId);
      if (project && collaborator) {
        project.collaborators.delete(userId);
        collaborator.projects.delete(projectId);
      }
    }
  
    getProjectCollaborators(projectId) {
      const project = this.projects.get(projectId);
      return project ? Array.from(project.collaborators).map(userId => this.collaborators.get(userId)) : [];
    }
  
    getUserProjects(userId) {
      const collaborator = this.collaborators.get(userId);
      return collaborator ? Array.from(collaborator.projects).map(projectId => this.projects.get(projectId)) : [];
    }
  }
  
  // Usage example:
  // const collaborationManager = new CollaborationManager();
  // collaborationManager.addCollaborator('user1', 'Alice', 'producer');
  // collaborationManager.addCollaborator('user2', 'Bob', 'musician');
  // collaborationManager.createProject('project1', 'Summer Hit', 'user1');
  // collaborationManager.addCollaboratorToProject('user2', 'project1');
  // console.log(collaborationManager.getProjectCollaborators('project1'));