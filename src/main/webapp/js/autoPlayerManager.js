class AudioPlayerManager {
    constructor() {
      this.audioContext = new (window.AudioContext || window.webkitAudioContext)();
      this.audioElement = document.createElement('audio');
      this.audioSource = this.audioContext.createMediaElementSource(this.audioElement);
      this.gainNode = this.audioContext.createGain();
      this.audioSource.connect(this.gainNode);
      this.gainNode.connect(this.audioContext.destination);
      this.playlist = [];
      this.currentTrackIndex = 0;
    }
  
    loadPlaylist(tracks) {
      this.playlist = tracks;
      this.currentTrackIndex = 0;
    }
  
    play() {
      if (this.playlist.length > 0) {
        this.audioElement.src = this.playlist[this.currentTrackIndex].url;
        this.audioElement.play();
      }
    }
  
    pause() {
      this.audioElement.pause();
    }
  
    stop() {
      this.audioElement.pause();
      this.audioElement.currentTime = 0;
    }
  
    next() {
      this.currentTrackIndex = (this.currentTrackIndex + 1) % this.playlist.length;
      this.play();
    }
  
    previous() {
      this.currentTrackIndex = (this.currentTrackIndex - 1 + this.playlist.length) % this.playlist.length;
      this.play();
    }
  
    setVolume(level) {
      this.gainNode.gain.setValueAtTime(level, this.audioContext.currentTime);
    }
  
    seekTo(time) {
      this.audioElement.currentTime = time;
    }
  
    getCurrentTime() {
      return this.audioElement.currentTime;
    }
  
    getDuration() {
      return this.audioElement.duration;
    }
  
    addEventListener(event, callback) {
      this.audioElement.addEventListener(event, callback);
    }
  }
  
  // Usage example:
  // const playerManager = new AudioPlayerManager();
  // playerManager.loadPlaylist([
  //   { url: 'path/to/track1.mp3', title: 'Track 1' },
  //   { url: 'path/to/track2.mp3', title: 'Track 2' }
  // ]);
  // playerManager.play();